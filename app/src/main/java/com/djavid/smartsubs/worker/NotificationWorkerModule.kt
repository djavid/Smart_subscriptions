package com.djavid.smartsubs.worker

import android.app.NotificationManager
import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class NotificationWorkerModule(context: Context) {
    val kodein = Kodein.Module("notification_worker_module") {
        bind<NotificationManager>() with singleton {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }
}