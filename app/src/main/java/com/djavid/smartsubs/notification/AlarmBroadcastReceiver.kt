package com.djavid.smartsubs.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.djavid.smartsubs.utils.KEY_AT_MILLIS
import com.djavid.smartsubs.utils.KEY_DAYS_UNTIL_SUBSCRIPTION_ENDS
import com.djavid.smartsubs.utils.KEY_NOTIFICATION_ID
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_TITLE
import com.djavid.smartsubs.worker.NotificationWorker

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println("onReceive: context {$context}\nintent {$intent}")

        if (intent != null && context != null) {
            val id = intent.getLongExtra(KEY_NOTIFICATION_ID, -1)
            val title = intent.getStringExtra(KEY_SUBSCRIPTION_TITLE)
            val daysUntil = intent.getLongExtra(KEY_DAYS_UNTIL_SUBSCRIPTION_ENDS, -1)
            val atMillis = intent.getLongExtra(KEY_AT_MILLIS, -1)

            val data = Data.Builder().putLong(KEY_NOTIFICATION_ID, id)
                .putString(KEY_SUBSCRIPTION_TITLE, title)
                .putLong(KEY_DAYS_UNTIL_SUBSCRIPTION_ENDS, daysUntil)
                .putLong(KEY_AT_MILLIS, atMillis)
                .build()

            val request = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(data)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }

}