package com.djavid.smartsubs.worker

import android.app.NotificationManager
import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

class NotificationWorkerModule(context: Context) {
    val di = DI.Module("notification_worker_module") {
        bind<NotificationManager>() with singleton {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }
}