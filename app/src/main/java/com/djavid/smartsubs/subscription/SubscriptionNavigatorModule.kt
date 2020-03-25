package com.djavid.smartsubs.subscription

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SubscriptionNavigatorModule {
    val kodein = Kodein.Module("subscription_navigator_module") {
        bind<SubscriptionContract.Navigator>() with singleton {
            SubscriptionNavigator(instance())
        }
    }
}