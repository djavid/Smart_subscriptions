package com.djavid.smartsubs.sub_list

import com.djavid.smartsubs.common.navigation.SubListNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubListNavigatorModule {
    val di = DI.Module("sub_list_navigator_module") {
        bind<SubListNavigator>() with singleton {
            SubListNavigatorImpl(instance())
        }
    }
}