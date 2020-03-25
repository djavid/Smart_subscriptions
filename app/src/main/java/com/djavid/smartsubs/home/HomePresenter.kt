package com.djavid.smartsubs.home

import com.djavid.smartsubs.create.CreateContract
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.getPriceInPeriod
import com.djavid.smartsubs.subscription.SubscriptionContract
import com.djavid.smartsubs.utils.SharedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: SubscriptionsRepository,
    private val createNavigator: CreateContract.Navigator,
    private val subNavigator: SubscriptionContract.Navigator,
    private val modelMapper: SubscriptionModelMapper,
    private val sharedPrefs: SharedRepository,
    coroutineScope: CoroutineScope
) : HomeContract.Presenter, CoroutineScope by coroutineScope {

    private var subs = mutableListOf<Subscription>()

    override fun init() {
        view.init(this)
        view.slidePanelToTop()
        view.setSubsPeriod(sharedPrefs.selectedSubsPeriod)
    }

    override fun reloadSubs() {
        launch {
            subs = repository.getSubs().map { modelMapper.fromDao(it) }.toMutableList()

            view.setSubsCount(subs.count())
            view.showSubs(subs, sharedPrefs.selectedSubsPeriod)
            view.setSubsPrice(calculateSubsPrice(), sharedPrefs.selectedCurrency)
        }
    }

    private fun calculateSubsPrice(): Double {
        val pricePeriod = sharedPrefs.selectedSubsPeriod
        return subs.sumByDouble { it.getPriceInPeriod(pricePeriod) }
    }

    override fun onItemClick(id: Long) {
        subNavigator.goToSubscription(id)
    }

    override fun onItemSwipedToLeft(position: Int) {
        launch {
            val subToRemove = subs[position]
            repository.deleteSubById(subToRemove.id)
            reloadSubs()
        }
    }

    override fun onAddSubPressed() {
        createNavigator.goToCreateScreen()
    }

    override fun onPeriodPressed() {
        val types = SubscriptionPeriodType.values()
        val index = (types.indexOf(sharedPrefs.selectedSubsPeriod) + 1).rem(types.size)
        sharedPrefs.selectedSubsPeriod = types[index]

        view.setSubsPeriod(sharedPrefs.selectedSubsPeriod)
        view.setSubsPrice(calculateSubsPrice(), sharedPrefs.selectedCurrency)
        view.updateListPrices(sharedPrefs.selectedSubsPeriod)
    }

}