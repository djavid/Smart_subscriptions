package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.common.CommonFragmentNavigator

class SubscriptionPresenter(
    private val view: SubscriptionContract.View,
    private val fragmentNavigator: CommonFragmentNavigator
) : SubscriptionContract.Presenter {

    override fun init(id: Long) {
        view.init(this)

        println(id)
    }

    override fun onBackPressed() {
        fragmentNavigator.goBack()
    }

}