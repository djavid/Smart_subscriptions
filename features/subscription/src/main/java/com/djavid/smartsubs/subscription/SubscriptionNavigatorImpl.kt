package com.djavid.smartsubs.subscription

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.core.ui.R
import com.djavid.smartsubs.common.SubscriptionNavigator
import com.djavid.smartsubs.common.utils.Constants

class SubscriptionNavigatorImpl(
    private val fragmentManager: FragmentManager
) : SubscriptionNavigator {

    override fun goToSubscription(id: String, isRoot: Boolean) {
        val bundle = Bundle().apply {
            putString(Constants.KEY_SUBSCRIPTION_ID, id)
            putBoolean(Constants.KEY_IS_ROOT, isRoot)
        }

        fragmentManager
            .beginTransaction()
            .add(
                R.id.fragmentContainer,
                SubscriptionFragment().apply { arguments = bundle },
                SubscriptionFragment::class.java.simpleName
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

}