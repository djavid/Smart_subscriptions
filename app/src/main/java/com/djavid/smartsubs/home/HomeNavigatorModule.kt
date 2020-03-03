package com.djavid.smartsubs.home

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class HomeNavigatorModule {
    val kodein = Kodein.Module("home_navigator_module") {
        bind<HomeContract.Navigator>() with singleton {
            HomeNavigator(instance())
        }
    }
}