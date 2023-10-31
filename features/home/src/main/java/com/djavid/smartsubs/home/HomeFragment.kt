package com.djavid.smartsubs.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.djavid.features.home.databinding.FragmentHomeBinding
import com.djavid.smartsubs.common.BaseFragment
import com.djavid.smartsubs.common.BroadcastHandler
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.subscribeApplicationReceiver
import com.djavid.smartsubs.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.instance

class HomeFragment : BaseFragment() {

    private val coroutineScope: CoroutineScope by instance()
    private val viewModel: HomeViewModel by instance()
    private val homeView: HomeView by instance()

    private lateinit var binding: FragmentHomeBinding

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == Constants.ACTION_REFRESH) {
                    viewModel.onRefreshAction()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentHomeBinding.inflate(inflater).apply {
            binding = this
            di = (activity?.application as SmartSubsApplication).homeComponent(this@HomeFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeView.onViewCreated(viewLifecycleOwner)
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        viewModel.onResume()

        subscribeApplicationReceiver(
            broadcastReceiver,
            listOf(Constants.ACTION_REFRESH), BroadcastHandler.Unsubscribe.ON_PAUSE
        )
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }

}