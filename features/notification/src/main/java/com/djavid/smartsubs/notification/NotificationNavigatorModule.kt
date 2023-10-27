package com.djavid.smartsubs.notification

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class NotificationNavigatorModule {
    val di = DI.Module("notification_navigator_module") {
        bind<NotificationContract.Navigator>() with singleton {
            NotificationNavigator(instance())
        }
    }
}