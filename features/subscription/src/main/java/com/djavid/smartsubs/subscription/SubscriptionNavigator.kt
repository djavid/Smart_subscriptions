package com.djavid.smartsubs.subscription

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R
import com.djavid.common.KEY_IS_ROOT
import com.djavid.common.KEY_SUBSCRIPTION_ID

class SubscriptionNavigator(
    private val fragmentManager: FragmentManager
) : SubscriptionContract.Navigator {

    override fun goToSubscription(id: String, isRoot: Boolean) {
        val bundle = Bundle().apply {
            putString(com.djavid.common.KEY_SUBSCRIPTION_ID, id)
            putBoolean(com.djavid.common.KEY_IS_ROOT, isRoot)
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