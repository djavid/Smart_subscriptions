package com.djavid.smartsubs.sub_list

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SubListNavigatorModule {
    val kodein = Kodein.Module("sub_list_navigator_module") {
        bind<SubListContract.Navigator>() with singleton {
            SubListNavigator(instance())
        }
    }
}