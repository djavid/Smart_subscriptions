package com.djavid.smartsubs.data.interactors

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

class CommonInteractorsModule {
    val di = DI.Module("common_interactors_module") {
        bind<GetSelectedCurrencyInteractor>() with provider { GetSelectedCurrencyInteractor(instance(), instance()) }
        bind<GetSortedSubsInteractor>() with provider { GetSortedSubsInteractor(instance(), instance()) }
        bind<GetSubsInteractor>() with provider { GetSubsInteractor(instance(), instance()) }
        bind<GetMostUsedCurrencyInteractor>() with provider { GetMostUsedCurrencyInteractor(instance()) }
        bind<GetPremiumSubscriptionStatusInteractor>() with provider { GetPremiumSubscriptionStatusInteractor() }
        bind<GetPredefinedSubsRootInteractor>() with provider { GetPredefinedSubsRootInteractor(instance()) }
    }
}