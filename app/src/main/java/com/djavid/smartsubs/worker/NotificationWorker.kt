package com.djavid.smartsubs.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.db.NotificationsRepository
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

    override fun doWork(): Result {
        val id = workerParams.inputData.getLong(KEY_NOTIFICATION_ID, -1)
        val title = workerParams.inputData.getString(KEY_SUBSCRIPTION_TITLE)
        val daysUntil = workerParams.inputData.getLong(KEY_DAYS_UNTIL_SUBSCRIPTION_ENDS, -1)
        val atMillis = workerParams.inputData.getLong(KEY_AT_MILLIS, -1)

        if (title?.isNotEmpty() == true && daysUntil > -1 && atMillis > -1 && id > -1) {
            val content = generateNotifContent(title, daysUntil)

            setupNotificationChannel()
            showSubExpiresNotification(sharedRepository.nextNotifId(), title, content)

            deactivateNotification(id)

            return Result.success()
        } else {
            return Result.failure()
        }
    }

    private fun deactivateNotification(id: Long) {
        runBlocking {
            repository.getNotificationById(id)?.let {
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

    private fun showSubExpiresNotification(notifId: Int, title: String, content: String) {
        val builder = NotificationCompat.Builder(context, SUBSCRIPTION_NOTIF_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round) //todo replace icon
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle())
//            .setContentIntent(createOpenQuestDetailsPendingIntent(questId)) todo add click action
            .setAutoCancel(false)

        notificationManager.notify(null, notifId, builder.build())
    }

//    todo add notif click action
//    private fun createSubScreenPendingIntent(): PendingIntent {
//        val intent = Intent(context, QuestOverviewActivity::class.java).apply {
//            putExtra(EXTRA_QUEST_ID, questId)
//            putExtra(EXTRA_APPLICATION_LOCALE, localeProvider.getLocale())
//        }
//        return TaskStackBuilder.create(context).addNextIntent(intent)
//            .getPendingIntent(questId.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT)!!
//    }

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