package com.djavid.smartsubs.interactors

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

class CommonInteractorsModule {
    val di = DI.Module("common_interactors_module") {
        bind<GetSelectedCurrencyInteractor>() with provider { GetSelectedCurrencyInteractor(instance()) }
        bind<GetSortedSubsInteractor>() with provider { GetSortedSubsInteractor(instance(), instance()) }
        bind<GetSubsInteractor>() with provider { GetSubsInteractor(instance(), instance()) }
    }
}