package com.djavid.smartsubs.data.mappers

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class MappersModule {
    val di = DI.Module("mappers_module") {
        bind<SubscriptionEntityMapper>() with singleton { SubscriptionEntityMapper() }
        bind<SubscriptionModelMapper>() with singleton { SubscriptionModelMapper(instance(), instance()) }
        bind<NotificationEntityMapper>() with singleton { NotificationEntityMapper() }
    }
}