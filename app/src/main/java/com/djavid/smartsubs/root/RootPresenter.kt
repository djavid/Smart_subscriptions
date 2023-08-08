package com.djavid.smartsubs.root

import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.home.HomeNavigator
import com.djavid.smartsubs.storage.RealTimeRepository
import com.djavid.smartsubs.subscription.SubscriptionContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RootPresenter(
    private val view: RootContract.View,
    private val homeNavigator: HomeNavigator,
    private val subNavigator: SubscriptionContract.Navigator,
    private val subsRepository: SubscriptionsRepository,
    private val realTimeRepository: RealTimeRepository,
    coroutineScope: CoroutineScope
) : RootContract.Presenter, CoroutineScope by coroutineScope {

    override fun init(subId: String?) {
        view.init(this)

        launch {
            moveSubsFromDbToFirebase()
            goNext(subId)
        }
    }

    private suspend fun moveSubsFromDbToFirebase() {
        val subsInDb = subsRepository.getSubs()

        if (subsInDb.isNotEmpty()) {
            if (realTimeRepository.saveSubs(subsInDb)) {
                subsRepository.deleteAllSubs()
            }
        }
    }

    private fun goNext(subId: String?) {
        if (subId != null) {
            subNavigator.goToSubscription(subId, true)
        } else {
            homeNavigator.goToHome()
        }
    }

}