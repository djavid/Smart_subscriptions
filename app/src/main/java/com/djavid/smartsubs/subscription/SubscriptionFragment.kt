package com.djavid.smartsubs.subscription

import android.os.Bundle
import android.view.View
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.common.BaseFragment
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.direct
import org.kodein.di.generic.instance

class SubscriptionFragment : BaseFragment(R.layout.fragment_subscription), BackPressListener {

    private val coroutineScope: CoroutineScope by instance()

    private lateinit var presenter: SubscriptionContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        kodein = (activity?.application as Application).subscription(this)
        presenter = kodein.direct.instance()

        arguments?.let {
            val id = it.getLong(KEY_SUBSCRIPTION_ID)
            presenter.init(id)
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }

}