package com.djavid.smartsubs.notification

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class AlarmNotifierModule {
    val di = DI.Module("alarm_notifier_module") {
        bind<AlarmNotifier>() with singleton {
            AlarmNotifier(instance(), instance())
        }
    }
}