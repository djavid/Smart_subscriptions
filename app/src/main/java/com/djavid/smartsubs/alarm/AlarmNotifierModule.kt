package com.djavid.smartsubs.alarm

import com.djavid.smartsubs.data.interactors.AlarmInteractor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class AlarmNotifierModule {
    val di = DI.Module("alarm_notifier_module") {
        bind<AlarmInteractor>() with singleton {
            AlarmNotifierImpl(instance(), instance())
        }
    }
}