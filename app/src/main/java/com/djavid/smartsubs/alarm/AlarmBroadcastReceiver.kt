package com.djavid.smartsubs.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.djavid.smartsubs.common.utils.Constants
import com.djavid.smartsubs.worker.NotificationWorker

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println("onReceive: context {$context}\nintent {$intent}")

        if (intent != null && context != null) {
            val id = intent.getLongExtra(Constants.KEY_NOTIFICATION_ID, -1)
            val data = Data.Builder().putLong(Constants.KEY_NOTIFICATION_ID, id).build()

            val request = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(data)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }

}