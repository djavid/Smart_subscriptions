package com.djavid.smartsubs.mappers

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class MappersModule {
    val kodein = Kodein.Module("mappers_module") {
        bind<SubscriptionMapper>() with singleton { SubscriptionMapper() }
    }
}