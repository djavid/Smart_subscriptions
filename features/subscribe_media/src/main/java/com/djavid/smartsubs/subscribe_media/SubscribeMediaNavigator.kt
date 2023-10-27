package com.djavid.smartsubs.subscribe_media

import androidx.fragment.app.FragmentManager

class SubscribeMediaNavigator(
    private val fm: FragmentManager
) : SubscribeMediaContract.Navigator {

    override fun showSubscribeDialog() {
        val fragment = SubscribeMediaDialog()
        fragment.show(fm, "subscribeMediaScreen")
    }

}