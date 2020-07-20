package com.djavid.smartsubs.sort

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SortNavigationModule {
    val kodein = Kodein.Module("sort_navigation_module") {
        bind<SortContract.Navigator>() with singleton {
            SortNavigator(instance())
        }
    }
}