package com.djavid.smartsubs.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.notification.AlarmNotifier
import com.djavid.smartsubs.root.RootActivity
import com.djavid.smartsubs.storage.SharedRepository
import com.djavid.smartsubs.utils.*
import kotlinx.coroutines.runBlocking
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class NotificationWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams), KodeinAware {

    override val kodein: Kodein
        get() = (context.applicationContext as Application).notificationWorkerComponent(context)

    private val notifRepository: NotificationsRepository by instance()
    private val subRepository: SubscriptionsRepository by instance()
    private val notificationManager: NotificationManager by instance()
    private val sharedRepository: SharedRepository by instance()
    private val alarmNotifier: AlarmNotifier by instance()

    private var model: Notification? = null
    private var subModel: SubscriptionDao? = null

    override fun doWork(): Result {
        val id = workerParams.inputData.getLong(KEY_NOTIFICATION_ID, -1)
        if (id == -1L) return Result.failure()

        loadNotification(id)
        loadSub(model?.subId)

        return model?.let { model ->
            subModel?.let { subModel ->
                showNotif(model, subModel)

                if (model.isRepeating) {
                    planNextAlarm()
                } else {
                    deleteNotification()
                }

                sendRefreshBroadcast()
                Result.success()
            }
        } ?: Result.failure()
    }

    private fun showNotif(model: Notification, subModel: SubscriptionDao) {
        val content = generateNotifContent(model.daysBefore)
        setupNotificationChannel()
        showSubExpiresNotification(model, subModel.title, sharedRepository.nextNotifId(), content)
    }

    private fun planNextAlarm() {
        val newModel = recalculateDateTime()

        if (newModel != null) {
            saveNotification(newModel)
            alarmNotifier.setAlarm(newModel)
        }
    }

    private fun recalculateDateTime(): Notification? {
        model?.let { model ->
            val paymentDate = subModel?.paymentDate
            val period = subModel?.period

            if (paymentDate != null && period != null) {
                val atDateTime = paymentDate.getFirstPeriodAfterNow(period).addPeriod(period)
                    .toDateTime(model.atDateTime.toLocalTime())
                    .minusDays(model.daysBefore.toInt())

                return model.copy(atDateTime = atDateTime)
            }
        }

        return null
    }

    private fun sendRefreshBroadcast() {
        val intent = Intent(ACTION_REFRESH)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun loadNotification(id: Long) {
        runBlocking {
            model = notifRepository.getNotificationById(id)
        }
    }

    private fun saveNotification(model: Notification) {
        runBlocking {
            notifRepository.editNotification(model)
        }
    }

    private fun loadSub(id: Long?) {
        if (id != null) {
            runBlocking {
                subModel = subRepository.getSubById(id)
            }
        }
    }

    private fun deleteNotification() {
        runBlocking {
            model?.let {
                notifRepository.deleteNotificationById(it.id)
            }
        }
    }

    private fun deactivateNotification() {
        runBlocking {
            model?.let {
                notifRepository.editNotification(it.copy(isActive = false))
            }
        }
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(R.string.subscription_channel_name)
            val channel = NotificationChannel(
                SUBSCRIPTION_NOTIF_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableLights(false)
            channel.enableVibration(false)

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showSubExpiresNotification(model: Notification, subTitle: String, notifId: Int, content: String) {
        val builder = NotificationCompat.Builder(context, SUBSCRIPTION_NOTIF_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setContentTitle(subTitle)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle())
            .setAutoCancel(false)
            .apply {
                val intent = createSubscriptionPendingIntent(model.subId)
                if (intent != null) {
                    setContentIntent(intent)
                }
            }

        notificationManager.notify(null, notifId, builder.build())
    }

    private fun createSubscriptionPendingIntent(subId: Long): PendingIntent? {
        val intent = Intent(context, RootActivity::class.java).apply {
            putExtra(KEY_SUBSCRIPTION_ID, subId)
        }

        val stackBuilder = TaskStackBuilder.create(context).apply {
            addNextIntent(intent)
        }

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun generateNotifContent(daysUntil: Long): String {
        return when {
            daysUntil == 0L -> { //today
                context.getString(R.string.title_notif_expires_today)
            }
            daysUntil == 1L -> { //tomorrow
                context.getString(R.string.title_notif_expires_tomorrow)
            }
            daysUntil > 1 -> { //days after
                val daysPlural = context.resources.getQuantityString(R.plurals.plural_day, daysUntil.toInt())
                context.getString(R.string.title_notif_expires_days_after, daysUntil, daysPlural)
            }
            else -> ""
        }
    }

}