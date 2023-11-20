package com.djavid.smartsubs.common

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class BasePipeline<T> {

    private val flow = MutableSharedFlow<T>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun postValue(value: T) = flow.tryEmit(value)

    suspend fun value(value: T) = flow.emit(value)

    fun getFlow(): SharedFlow<T> = flow

}