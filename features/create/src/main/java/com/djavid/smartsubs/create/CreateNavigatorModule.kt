package com.djavid.smartsubs.create

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class CreateNavigatorModule {
    val di = DI.Module("create_navigator_module") {
        bind<CreateContract.Navigator>() with singleton {
            CreateNavigator(instance())
        }
    }
}