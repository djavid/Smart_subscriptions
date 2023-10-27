package com.djavid.smartsubs.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.djavid.smartsubs.models.Notification
import com.djavid.common.ACTION_ALARM
import com.djavid.common.KEY_NOTIFICATION_ID
import kotlinx.coroutines.CoroutineScope

class AlarmNotifier(
    private val context: Context,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun setAlarm(model: Notification) {
        if (alarmManager != null) {
            val pendingIntent = getPendingIntentToFire(model.id)

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager, AlarmManager.RTC_WAKEUP, model.atDateTime.millis, pendingIntent
            )
        }
    }

    private fun getPendingIntentToFire(notifId: Long): PendingIntent {
        val broadcastReceiverIntent = getBroadcastReceiverIntent(notifId)

        return PendingIntent.getBroadcast(
            context, notifId.toInt(), broadcastReceiverIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getBroadcastReceiverIntent(id: Long): Intent {
        return Intent(context, AlarmBroadcastReceiver::class.java).apply {
            action = com.djavid.common.ACTION_ALARM + id
            putExtra(com.djavid.common.KEY_NOTIFICATION_ID, id)
        }
    }

    fun cancelAlarm(notifId: Long) {
        val intent = getPendingIntentToFire(notifId)
        alarmManager?.cancel(intent)
    }

}