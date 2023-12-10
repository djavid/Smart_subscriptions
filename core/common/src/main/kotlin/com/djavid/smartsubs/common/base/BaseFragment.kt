package com.djavid.smartsubs.common.base

import android.content.BroadcastReceiver
import androidx.fragment.app.Fragment
import com.djavid.smartsubs.common.utils.BroadcastHandler
import com.djavid.smartsubs.common.utils.clearReceiver
import com.djavid.smartsubs.common.utils.clearReceivers
import org.kodein.di.DI
import org.kodein.di.DIAware

abstract class BaseFragment : Fragment(), DIAware, BroadcastHandler {

    override lateinit var di: DI

    override val receivers = arrayListOf<Triple<BroadcastReceiver, BroadcastHandler.Scope, BroadcastHandler.Unsubscribe>>()

    override fun onPause() {
        super.onPause()
        clearReceiver(BroadcastHandler.Unsubscribe.ON_PAUSE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearReceiver(BroadcastHandler.Unsubscribe.ON_DESTROY_VIEW)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearReceivers()
    }

}