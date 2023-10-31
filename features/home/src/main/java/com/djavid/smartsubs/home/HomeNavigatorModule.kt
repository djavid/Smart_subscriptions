package com.djavid.smartsubs.home

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import com.djavid.smartsubs.common.HomeNavigator

class HomeNavigatorModule {
    val di = DI.Module("home_navigator_module") {
        bind<HomeNavigator>() with singleton {
            HomeNavigatorImpl(instance())
        }
    }
}