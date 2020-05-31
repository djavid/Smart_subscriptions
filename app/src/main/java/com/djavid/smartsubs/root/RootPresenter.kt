package com.djavid.smartsubs.root

import com.djavid.smartsubs.home.HomeContract
import com.djavid.smartsubs.subscription.SubscriptionContract

class RootPresenter(
    private val view: RootContract.View,
    private val homeNavigator: HomeContract.Navigator,
    private val subNavigator: SubscriptionContract.Navigator
) : RootContract.Presenter {

    override fun init(subId: Long?) {
        view.init(this)

        if (subId != null) {
            goToSubscription(subId)
        } else {
            goToHomeScreen()
        }
    }

    override fun goToHomeScreen() {
        homeNavigator.goToHome()
    }

    private fun goToSubscription(subId: Long) {
        subNavigator.goToSubscription(subId, true)
    }

}