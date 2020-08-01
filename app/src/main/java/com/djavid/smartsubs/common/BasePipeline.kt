package com.djavid.smartsubs.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

@OptIn(ExperimentalCoroutinesApi::class)
class BasePipeline<T> {

    private val ch = BroadcastChannel<T>(Channel.BUFFERED)

    fun postValue(value: T) {
        ch.offer(value)
    }

    fun subscribe(): ReceiveChannel<T> {
        return ch.openSubscription()
    }

}