package com.djavid.smartsubs.sub_list

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubListNavigatorModule {
    val di = DI.Module("sub_list_navigator_module") {
        bind<SubListContract.Navigator>() with singleton {
            SubListNavigator(instance())
        }
    }
}