package com.djavid.smartsubs.root

import android.content.Context
import com.djavid.smartsubs.home.HomeContract
import com.djavid.smartsubs.subscription.SubscriptionContract
import com.djavid.smartsubs.worker.UploaderWorker
import kotlinx.coroutines.CoroutineScope

class RootPresenter(
    private val view: RootContract.View,
    private val homeNavigator: HomeContract.Navigator,
    private val subNavigator: SubscriptionContract.Navigator,
    private val appContext: Context,
    coroutineScope: CoroutineScope
) : RootContract.Presenter, CoroutineScope by coroutineScope {

    override fun init(subId: Long?) {
        view.init(this)
        UploaderWorker.enqueueWork(appContext)

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