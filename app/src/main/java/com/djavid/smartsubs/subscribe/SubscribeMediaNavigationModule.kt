package com.djavid.smartsubs.subscribe

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubscribeMediaNavigationModule {
    val di = DI.Module("subscribe_media_module") {
        bind<SubscribeMediaContract.Navigator>() with singleton {
            SubscribeMediaNavigator(instance())
        }
    }
}