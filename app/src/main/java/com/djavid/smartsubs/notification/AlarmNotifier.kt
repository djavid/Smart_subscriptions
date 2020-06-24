package com.djavid.smartsubs.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.utils.ACTION_ALARM
import com.djavid.smartsubs.utils.KEY_NOTIFICATION_ID
import kotlinx.coroutines.CoroutineScope

class AlarmNotifier(
    private val context: Context,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun setAlarm(model: Notification) {
        if (alarmManager != null) {
            val pendingIntent = getPendingIntentToFire(model.id)

            println("setAlarm: $model")
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager, AlarmManager.RTC_WAKEUP, model.atDateTime.millis, pendingIntent
            )
        }
    }

    private fun getPendingIntentToFire(notifId: Long): PendingIntent {
        val broadcastReceiverIntent = getBroadcastReceiverIntent(notifId)

        return PendingIntent.getBroadcast(
            context, 0, broadcastReceiverIntent, PendingIntent.FLAG_ONE_SHOT
        )
    }

    private fun getBroadcastReceiverIntent(id: Long): Intent {
        return Intent(context, AlarmBroadcastReceiver::class.java).apply {
            action = ACTION_ALARM
            putExtra(KEY_NOTIFICATION_ID, id)
        }
    }

    fun cancelAlarm(notifId: Long) {
        val intent = getPendingIntentToFire(notifId)
        alarmManager?.cancel(intent)
    }

}