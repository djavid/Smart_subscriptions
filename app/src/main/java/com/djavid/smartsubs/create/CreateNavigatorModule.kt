package com.djavid.smartsubs.create

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class CreateNavigatorModule {
    val kodein = Kodein.Module("create_navigator_module") {
        bind<CreateContract.Navigator>() with singleton {
            CreateNavigator(instance())
        }
    }
}