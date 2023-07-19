package com.djavid.smartsubs.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class BasePipeline<T> {

    private val flow = MutableSharedFlow<T>()

    fun postValue(value: T) {
        flow.tryEmit(value)
    }

    fun getFlow(): SharedFlow<T> = flow

}