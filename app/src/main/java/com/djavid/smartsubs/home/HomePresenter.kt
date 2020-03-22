package com.djavid.smartsubs.home

import android.view.View
import com.djavid.smartsubs.create.CreateContract
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.Currency
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: SubscriptionsRepository,
    private val createNavigator: CreateContract.Navigator,
    private val modelMapper: SubscriptionModelMapper,
    coroutineScope: CoroutineScope
) : HomeContract.Presenter, CoroutineScope by coroutineScope {

    private var selectedSubPeriod = SubscriptionPeriodType.MONTH //todo get from sharedPrefs

    override fun init() {
        view.init(this)

        launch {
            //            val subs = repository.getSubs().map { mapper.fromDao(it) } todo
            val subs = listOf<SubscriptionDao>(
                SubscriptionDao(
                    0,
                    "EA Access",
                    1799.0,
                    Currency.RUB,
                    SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1),
                    DateTime().minusDays(13),
                    null
                ),
                SubscriptionDao(
                    0,
                    "EA Access",
                    1799.0,
                    Currency.RUB,
                    SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1),
                    DateTime().minusDays(13),
                    null
                ), SubscriptionDao(
                    0,
                    "EA Access",
                    1799.0,
                    Currency.RUB,
                    SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1),
                    DateTime().minusDays(13),
                    null
                ), SubscriptionDao(
                    0,
                    "EA Access",
                    1799.0,
                    Currency.RUB,
                    SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1),
                    DateTime().minusDays(13),
                    null
                ), SubscriptionDao(
                    0,
                    "EA Access",
                    1799.0,
                    Currency.RUB,
                    SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1),
                    DateTime().minusDays(13),
                    null
                ),
                SubscriptionDao(
                    0,
                    "EA Access",
                    1799.0,
                    Currency.RUB,
                    SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1),
                    DateTime().minusDays(13),
                    null
                ),
                SubscriptionDao(
                    0,
                    "EA Access",
                    1799.0,
                    Currency.RUB,
                    SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1),
                    DateTime().minusDays(13),
                    null
                )
            ).map { modelMapper.fromDao(it) }

            view.slidePanelToTop()
            view.showSubs(subs)
            view.setSubsPeriod(selectedSubPeriod)
            view.setSubsCount(subs.count())
            view.setSubsPrice(subs.sumByDouble { it.price }, Currency.RUB) //todo currency
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