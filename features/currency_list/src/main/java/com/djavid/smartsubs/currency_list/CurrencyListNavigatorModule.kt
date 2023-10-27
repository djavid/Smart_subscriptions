package com.djavid.smartsubs.currency_list

import com.djavid.smartsubs.currency_list.CurrencyListContract
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