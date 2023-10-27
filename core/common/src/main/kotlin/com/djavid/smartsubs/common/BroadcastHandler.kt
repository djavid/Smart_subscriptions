package com.djavid.smartsubs.common

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager

interface BroadcastHandler {
    enum class Scope {
        APPLICATION, ACTIVITY
    }

    enum class Unsubscribe {
        ON_PAUSE, ON_DESTROY_VIEW
    }

    val receivers: ArrayList<Triple<BroadcastReceiver, Scope, Unsubscribe>>
}

fun BroadcastHandler.subscribeApplicationReceiver(
    receiver: BroadcastReceiver, actions: List<String>, unsubscribe: BroadcastHandler.Unsubscribe
) {
    val broadcastManager = LocalBroadcastManager.getInstance(requireContext())
    receivers.add(Triple(receiver, BroadcastHandler.Scope.APPLICATION, unsubscribe))
    val intentFilter = IntentFilter().apply {
        actions.forEach { addAction(it) }
    }
    broadcastManager.registerReceiver(receiver, intentFilter)
}

fun BroadcastHandler.subscribeActivityReceiver(
    receiver: BroadcastReceiver, intentFilter: IntentFilter, unsubscribe: BroadcastHandler.Unsubscribe
) {
    receivers.add(Triple(receiver, BroadcastHandler.Scope.ACTIVITY, unsubscribe))
    requireContext().registerReceiver(receiver, intentFilter)
}

private fun BroadcastHandler.requireContext(): Context {
    return when(this) {
        is Activity -> this
        is Fragment -> this.requireActivity()
        else -> throw IllegalStateException("${this::class.java} is not expected")
    }
}

internal fun BroadcastHandler.clearReceivers() {
    receivers.forEach {
        when (it.second) {
            BroadcastHandler.Scope.APPLICATION -> LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(it.first)
            BroadcastHandler.Scope.ACTIVITY -> requireContext().unregisterReceiver(it.first)
        }
    }
    receivers.clear()
}

internal fun BroadcastHandler.clearReceiver(unsubscribe: BroadcastHandler.Unsubscribe) {
    receivers.forEach {
        if (it.third == unsubscribe) {
            when (it.second) {
                BroadcastHandler.Scope.APPLICATION -> LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(it.first)
                BroadcastHandler.Scope.ACTIVITY -> requireContext().unregisterReceiver(it.first)
            }
        }
    }
    receivers.removeAll { it.third == unsubscribe }
}