package com.djavid.smartsubs.sort

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SortNavigationModule {
    val di = DI.Module("sort_navigation_module") {
        bind<SortContract.Navigator>() with singleton {
            SortNavigator(instance())
        }
    }
}