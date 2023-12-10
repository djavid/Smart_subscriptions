package com.djavid.smartsubs.notification

import com.djavid.smartsubs.common.navigation.NotificationNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class NotificationNavigatorModule {
    val di = DI.Module("notification_navigator_module") {
        bind<NotificationNavigator>() with singleton {
            NotificationNavigatorImpl(instance())
        }
    }
}