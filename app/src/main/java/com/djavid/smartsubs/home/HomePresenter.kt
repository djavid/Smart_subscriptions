package com.djavid.smartsubs.home

import com.djavid.smartsubs.create.CreateContract
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.utils.SharedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: SubscriptionsRepository,
    private val createNavigator: CreateContract.Navigator,
    private val modelMapper: SubscriptionModelMapper,
    private val sharedPrefs: SharedRepository,
    coroutineScope: CoroutineScope
) : HomeContract.Presenter, CoroutineScope by coroutineScope {

    private var subs = mutableListOf<Subscription>()
    private var selectedSubPeriod = SubscriptionPeriodType.MONTH //todo get from sharedPrefs

    override fun init() {
        view.init(this)

        view.slidePanelToTop()
        view.setSubsPeriod(selectedSubPeriod)
    }

    override fun reloadSubs() {
        launch {
            val selectedCurrency = Currency.getInstance(sharedPrefs.selectedCurrencyCode)
            subs = repository.getSubs().map { modelMapper.fromDao(it) }.toMutableList()

            view.setSubsCount(subs.count())
            view.showSubs(subs)
            view.setSubsPrice(subs.sumByDouble { it.price }, selectedCurrency)
        }
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
        //todo
        println("onPeriodPressed")
    }

}