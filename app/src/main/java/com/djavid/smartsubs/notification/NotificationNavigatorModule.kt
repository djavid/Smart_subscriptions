package com.djavid.smartsubs.notification

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class NotificationNavigatorModule {
    val kodein = Kodein.Module("notification_navigator_module") {
        bind<NotificationContract.Navigator>() with singleton {
            NotificationNavigator(instance())
        }
    }
}