package com.djavid.smartsubs.sort

import com.djavid.smartsubs.common.SortNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SortNavigationModule {
    val di = DI.Module("sort_navigation_module") {
        bind<SortNavigator>() with singleton {
            SortNavigatorImpl(instance())
        }
    }
}