package com.djavid.smartsubs.subscription

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID

class SubscriptionNavigator(
    private val fragmentManager: FragmentManager
) : SubscriptionContract.Navigator {

    override fun goToSubscription(id: Long) {
        val bundle = Bundle().apply {
            putLong(KEY_SUBSCRIPTION_ID, id)
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