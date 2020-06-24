package com.djavid.smartsubs.notification

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AlarmNotifierModule {
    val kodein = Kodein.Module("alarm_notifier_module") {
        bind<AlarmNotifier>() with singleton {
            AlarmNotifier(instance(), instance())
        }
    }
}