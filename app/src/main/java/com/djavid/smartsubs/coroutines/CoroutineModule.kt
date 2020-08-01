package com.djavid.smartsubs.coroutines

import com.djavid.smartsubs.common.BasePipeline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CoroutineModule {
    val kodein = Kodein.Module("coroutine_module") {
        bind<CoroutineScope>() with provider { CancelableCoroutineScope(Dispatchers.Main) }
        bind<BasePipeline<Pair<String, String>>>() with singleton {
            BasePipeline<Pair<String, String>>()
        }
    }
}