package com.djavid.smartsubs.common.coroutines

import com.djavid.smartsubs.common.base.BasePipeline
import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

class CoroutineModule {
    val di = DI.Module("coroutine_module") {
        bind<CoroutineScope>() with provider { CancelableCoroutineScope(Dispatchers.Main) }
        bind<BasePipeline<Pair<String, String>>>() with singleton { BasePipeline() }
        bind<BasePipeline<PredefinedSuggestionItem>>() with singleton { BasePipeline() }
    }
}