package com.djavid.smartsubs.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.djavid.smartsubs.utils.*

class AlarmNotifier(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun scheduleNotification(id: Long, title: String, daysUntil: Long, atMillis: Long) {
        alarmManager?.let {
            val broadcastReceiverIntent = getBroadcastReceiverIntent(id, title, daysUntil, atMillis)
            val pendingIntent = PendingIntent.getBroadcast(context, 0,
                broadcastReceiverIntent, PendingIntent.FLAG_ONE_SHOT)

            AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, atMillis, pendingIntent)
        }
    }

    private fun getBroadcastReceiverIntent(id: Long, title: String, daysUntil: Long, atMillis: Long): Intent {
        return Intent(context, AlarmBroadcastReceiver::class.java).apply {
            action = ACTION_ALARM
            putExtra(KEY_NOTIFICATION_ID, id)
            putExtra(KEY_SUBSCRIPTION_TITLE, title)
            putExtra(KEY_DAYS_UNTIL_SUBSCRIPTION_ENDS, daysUntil)
            putExtra(KEY_AT_MILLIS, atMillis)
        }
    }

}