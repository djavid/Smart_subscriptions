package com.djavid.smartsubs.notifications

import com.djavid.smartsubs.common.NotificationsNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class NotificationsNavigatorModule {
    val di = DI.Module("notifications_navigator_module") {
        bind<NotificationsNavigator>() with singleton {
            NotificationsNavigatorImpl(instance())
        }
    }
}