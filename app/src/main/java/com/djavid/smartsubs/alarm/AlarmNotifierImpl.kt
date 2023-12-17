package com.djavid.smartsubs.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.djavid.smartsubs.common.domain.Notification
import com.djavid.smartsubs.data.interactors.AlarmInteractor
import com.djavid.smartsubs.common.utils.Constants
import kotlinx.coroutines.CoroutineScope

class AlarmNotifierImpl(
    private val context: Context,
    coroutineScope: CoroutineScope
) : AlarmInteractor, CoroutineScope by coroutineScope {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    override fun setAlarm(model: Notification) {
        if (alarmManager != null) {
            val pendingIntent = getPendingIntentToFire(model.id)

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager, AlarmManager.RTC_WAKEUP, model.atDateTime.millis, pendingIntent
            )
        }
    }

    override fun cancelAlarm(notifId: Long) {
        val intent = getPendingIntentToFire(notifId)
        alarmManager?.cancel(intent)
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
            action = Constants.ACTION_ALARM + id
            putExtra(Constants.KEY_NOTIFICATION_ID, id)
        }
    }

}