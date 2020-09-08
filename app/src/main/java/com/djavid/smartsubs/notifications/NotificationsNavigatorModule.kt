package com.djavid.smartsubs.notifications

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class NotificationsNavigatorModule {
    val kodein = Kodein.Module("notifications_navigator_module") {
        bind<NotificationsContract.Navigator>() with singleton {
            NotificationsNavigator(instance())
        }
    }
}