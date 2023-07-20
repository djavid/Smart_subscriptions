package com.djavid.smartsubs.subscription

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.common.BaseFragment
import com.djavid.smartsubs.common.BroadcastHandler
import com.djavid.smartsubs.common.subscribeApplicationReceiver
import com.djavid.smartsubs.databinding.FragmentSubscriptionBinding
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.utils.KEY_IS_ROOT
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.instance

class SubscriptionFragment : BaseFragment(R.layout.fragment_subscription), BackPressListener {

    private val coroutineScope: CoroutineScope by instance()

    private val presenter: SubscriptionContract.Presenter by instance()
    private lateinit var binding: FragmentSubscriptionBinding

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == ACTION_REFRESH) {
                    presenter.reload(false)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSubscriptionBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as Application).subscriptionComponent(this@SubscriptionFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val id = it.getString(KEY_SUBSCRIPTION_ID)
            val isRoot = it.getBoolean(KEY_IS_ROOT)

            presenter.init(id, isRoot)
        }
    }

    override fun onResume() {
        super.onResume()

        subscribeApplicationReceiver(
            broadcastReceiver,
            listOf(ACTION_REFRESH), BroadcastHandler.Unsubscribe.ON_PAUSE
        )
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }

}