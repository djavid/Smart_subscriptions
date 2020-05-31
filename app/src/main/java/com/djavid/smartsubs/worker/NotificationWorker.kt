package com.djavid.smartsubs.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.root.RootActivity
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

    private val repository: NotificationsRepository by instance()
    private val notificationManager: NotificationManager by instance()
    private val sharedRepository: SharedRepository by instance()

    private var model: Notification? = null

    override fun doWork(): Result {
        val id = workerParams.inputData.getLong(KEY_NOTIFICATION_ID, -1)
        val title = workerParams.inputData.getString(KEY_SUBSCRIPTION_TITLE)
        val daysUntil = workerParams.inputData.getLong(KEY_DAYS_UNTIL_SUBSCRIPTION_ENDS, -1)
        val atMillis = workerParams.inputData.getLong(KEY_AT_MILLIS, -1)

        return if (title?.isNotEmpty() == true && daysUntil > -1 && atMillis > -1 && id > -1) {
            loadNotification(id)

            if (model != null) {
                val content = generateNotifContent(title, daysUntil)
                setupNotificationChannel()
                showSubExpiresNotification(model!!, title, sharedRepository.nextNotifId(), content)

                deactivateNotification()

                Result.success()
            } else {
                Result.failure()
            }
        } else {
            Result.failure()
        }
    }

    private fun loadNotification(id: Long) {
        runBlocking {
            model = repository.getNotificationById(id)
        }
    }

    private fun deactivateNotification() {
        runBlocking {
            model?.let {
                repository.editNotification(it.copy(isActive = false))
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
            .setSmallIcon(R.mipmap.ic_launcher_round) //todo replace icon
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setContentTitle(subTitle)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(createSubscriptionPendingIntent(model.subId))
            .setAutoCancel(false)

        notificationManager.notify(null, notifId, builder.build())
    }

    private fun createSubscriptionPendingIntent(subId: Long): PendingIntent {
        val intent = Intent(context, RootActivity::class.java).apply {
            putExtra(KEY_SUBSCRIPTION_ID, subId)
        }

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun generateNotifContent(subTitle: String, daysUntil: Long): String {
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