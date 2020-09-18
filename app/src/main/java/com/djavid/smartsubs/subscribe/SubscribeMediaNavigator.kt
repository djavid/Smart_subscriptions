package com.djavid.smartsubs.subscribe

import androidx.fragment.app.FragmentManager

class SubscribeMediaNavigator(
    private val fm: FragmentManager
) : SubscribeMediaContract.Navigator {

    override fun showSubscribeDialog() {
        val fragment = SubscribeMediaDialog()
        fragment.show(fm, "subscribeMediaScreen")
    }

}