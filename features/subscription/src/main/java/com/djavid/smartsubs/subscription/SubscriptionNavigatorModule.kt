package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.common.SubscriptionNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubscriptionNavigatorModule {
    val di = DI.Module("subscription_navigator_module") {
        bind<SubscriptionNavigator>() with singleton {
            SubscriptionNavigatorImpl(instance())
        }
    }
}