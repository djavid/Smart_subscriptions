package com.djavid.smartsubs.currencyList

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class CurrencyListNavigatorModule {
    val di = DI.Module("currency_list_navigator_module") {
        bind<CurrencyListContract.Navigator>() with singleton {
            CurrencyListNavigator(instance())
        }
    }
}