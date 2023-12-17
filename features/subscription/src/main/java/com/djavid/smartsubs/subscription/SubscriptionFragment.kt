package com.djavid.smartsubs.subscription

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.activity.addCallback
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.features.subscription.databinding.FragmentSubscriptionBinding
import com.djavid.smartsubs.common.base.BaseFragment
import com.djavid.smartsubs.common.utils.BroadcastHandler
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.utils.subscribeApplicationReceiver
import com.djavid.smartsubs.common.utils.Constants
import org.kodein.di.instance

class SubscriptionFragment : BaseFragment() {

    private val presenter: SubscriptionContract.Presenter by instance()
    private val view: SubscriptionContract.View by instance()
    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == Constants.ACTION_REFRESH) {
                    presenter.reload(false)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentSubscriptionBinding.inflate(inflater).apply {
            _binding = this
            di = (requireActivity().application as SmartSubsApplication).subscriptionComponent(
                this@SubscriptionFragment,
                binding
            )
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val id = it.getString(Constants.KEY_SUBSCRIPTION_ID)
            val isRoot = it.getBoolean(Constants.KEY_IS_ROOT)

            presenter.init(id, isRoot)
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) { presenter.goBack() }
    }

    override fun onResume() {
        super.onResume()

        subscribeApplicationReceiver(
            broadcastReceiver,
            listOf(Constants.ACTION_REFRESH), BroadcastHandler.Unsubscribe.ON_PAUSE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view.destroy()
        _binding = null
    }
}