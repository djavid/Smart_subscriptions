package com.djavid.smartsubs.home

import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.mappers.SubscriptionMapper
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriodType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: SubscriptionsRepository,
    private val mapper: SubscriptionMapper,
    coroutineScope: CoroutineScope
) : HomeContract.Presenter, CoroutineScope by coroutineScope {

    private var selectedSubPeriod = SubscriptionPeriodType.MONTH //todo get from sharedPrefs

    override fun init() {
        view.init(this)

        launch {
//            val subs = repository.getSubs().map { mapper.fromDao(it) } todo
            val subs = listOf<SubscriptionDao>(
                SubscriptionDao(0, "", 23.0,  "", null, null, null),
                SubscriptionDao(0, "", 23.0,  "", null, null, null),
                SubscriptionDao(0, "", 23.0,  "", null, null, null),
                SubscriptionDao(0, "", 23.0,  "", null, null, null),
                SubscriptionDao(0, "", 23.0,  "", null, null, null)
            ).map { mapper.fromDao(it) }
            view.showSubs(subs)
            view.setSubsPeriod(selectedSubPeriod)
            view.setSubsCount(subs.count())
            view.setSubsPrice(subs.sumByDouble { it.price })
        }
    }

    override fun onAddSubPressed() {
        //todo
        println("onAddSubPressed")
    }

    override fun onPeriodPressed() {
        //todo
        println("onPeriodPressed")
    }

}