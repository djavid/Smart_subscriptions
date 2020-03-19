package com.djavid.smartsubs.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class CoroutineModule {
    val kodein = Kodein.Module("coroutine_module") {
        bind<CoroutineScope>() with provider { CancelableCoroutineScope(Dispatchers.Main) }
    }
}