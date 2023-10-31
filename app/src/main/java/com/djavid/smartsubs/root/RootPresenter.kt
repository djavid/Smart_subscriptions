package com.djavid.smartsubs.root

import com.djavid.smartsubs.common.HomeNavigator
import com.djavid.smartsubs.common.SubscriptionNavigator
import com.djavid.smartsubs.data.db.SubscriptionsRepository
import com.djavid.smartsubs.data.storage.RealTimeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RootPresenter(
    private val view: RootContract.View,
    private val homeNavigator: HomeNavigator,
    private val subNavigator: SubscriptionNavigator,
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