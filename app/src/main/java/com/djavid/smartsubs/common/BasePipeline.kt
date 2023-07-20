package com.djavid.smartsubs.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class BasePipeline<T> {

    private val flow = MutableSharedFlow<T>()

    suspend fun postValue(value: T) {
        flow.emit(value)
    }

    fun getFlow(): SharedFlow<T> = flow

}