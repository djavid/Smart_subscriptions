package com.djavid.smartsubs.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BaseFragment
import com.djavid.smartsubs.common.BroadcastHandler
import com.djavid.smartsubs.common.subscribeApplicationReceiver
import com.djavid.smartsubs.utils.ACTION_REFRESH_LIST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.direct
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val coroutineScope: CoroutineScope by instance()

    private lateinit var presenter: HomeContract.Presenter

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == ACTION_REFRESH_LIST) {
                    presenter.reloadSubs()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        kodein = (activity?.application as Application).homeComponent(this)
        presenter = kodein.direct.instance()
        presenter.init()
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        presenter.reloadSubs()

        subscribeApplicationReceiver(
            broadcastReceiver,
            listOf(ACTION_REFRESH_LIST), BroadcastHandler.Unsubscribe.ON_PAUSE
        )
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }

}