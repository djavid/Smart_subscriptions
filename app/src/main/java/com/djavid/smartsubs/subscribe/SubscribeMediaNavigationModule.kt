package com.djavid.smartsubs.subscribe

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SubscribeMediaNavigationModule {
    val kodein = Kodein.Module("subscribe_media_module") {
        bind<SubscribeMediaContract.Navigator>() with singleton {
            SubscribeMediaNavigator(instance())
        }
    }
}