package com.djavid.smartsubs.home

import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.create.CreateContract
import com.djavid.smartsubs.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.*
import com.djavid.smartsubs.sort.SortContract
import com.djavid.smartsubs.subscribe.SubscribeMediaContract
import com.djavid.smartsubs.subscription.SubscriptionContract
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.storage.RealTimeRepository
import com.djavid.smartsubs.storage.SharedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.*

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: RealTimeRepository,
    private val createNavigator: CreateContract.Navigator,
    private val sortNavigator: SortContract.Navigator,
    private val subNavigator: SubscriptionContract.Navigator,
    private val modelMapper: SubscriptionModelMapper,
    private val sharedPrefs: SharedRepository,
    private val pipeline: BasePipeline<Pair<String, String>>,
    private val logger: FirebaseLogger,
    private val subscribeMediaNavigator: SubscribeMediaContract.Navigator,
    private val inAppReview: InAppReview,
    coroutineScope: CoroutineScope
) : HomeContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var channel: ReceiveChannel<Pair<String, String>>
    private var subs = listOf<Subscription>()

    override fun init() {
        view.init(this)
        view.slidePanelToTop()
        view.setSubsPeriod(sharedPrefs.selectedSubsPeriod)

        if (sharedPrefs.firstTimeOpened) {
            onFirstOpen()
        }

        listenPipeline()
    }

    private fun onFirstOpen() {
        sharedPrefs.firstTimeOpened = false
    }

    private fun showTgDialog() {
        subscribeMediaNavigator.showSubscribeDialog()
    }

    private fun showInAppReview() {
        inAppReview.showInAppDialog()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun listenPipeline() {
        launch {
            channel = pipeline.subscribe()
            channel.consumeEach {
                when (it.first) {
                    ACTION_REFRESH -> {
                        reloadSubs()

                        if (sharedPrefs.tgDialogTimesShown == 0) {
                            showTgDialog()
                        } else if (sharedPrefs.tgInAppReviewTimesShown == 0) {
                            showInAppReview()
                        }
                    }
                }
            }
        }
    }

    override fun reloadSubs() {
        launch {
            repository.updateTrialSubs()
            subs = repository.getSubs().map { modelMapper.fromDao(it) }.toMutableList()
            subs = applySortPreferences(subs)

            view.setSubsCount(subs.count())

            val price = SubscriptionPrice(calculateSubsPrice(), sharedPrefs.selectedCurrency)
            view.setSubsPrice(price)

            view.showEmptyPlaceholder(subs.isEmpty())

            if (subs.isNotEmpty()) {
                view.showSubs(subs, sharedPrefs.selectedSubsPeriod)
            }
        }
    }

    private fun applySortPreferences(subs: List<Subscription>): List<Subscription> {
        val asc = sharedPrefs.selectedSortType == SortType.ASC

        return if (asc) {
            when (sharedPrefs.selectedSortBy) {
                SortBy.TITLE -> subs.sortedBy { it.title.toLowerCase(Locale.getDefault()) }
                SortBy.PRICE -> subs.sortedBy { it.getPriceInPeriod(sharedPrefs.selectedSubsPeriod) }
                SortBy.DAYS_UNTIL -> subs.sortedBy { it.progress?.daysLeft }
                SortBy.CREATION_DATE -> subs.sortedBy { it.id }
            }
        } else {
            when (sharedPrefs.selectedSortBy) {
                SortBy.TITLE -> subs.sortedByDescending { it.title.toLowerCase(Locale.getDefault()) }
                SortBy.PRICE -> subs.sortedByDescending { it.getPriceInPeriod(sharedPrefs.selectedSubsPeriod) }
                SortBy.DAYS_UNTIL -> subs.sortedByDescending { it.progress?.daysLeft }
                SortBy.CREATION_DATE -> subs.sortedByDescending { it.id }
            }
        }
    }

    private fun calculateSubsPrice(): Double {
        val pricePeriod = sharedPrefs.selectedSubsPeriod
        return subs.filter { !it.isTrial() }.sumByDouble { it.getPriceInPeriod(pricePeriod) }
    }

    override fun onItemClick(id: String) {
        subNavigator.goToSubscription(id)

        subs.find { it.id == id }?.let {
            logger.subItemClicked(it)
        }
    }

    override fun onItemSwipedToLeft(position: Int) {
        launch {
            val subToRemove = subs[position]
            logger.subDelete(subToRemove)
            logger.onSubItemSwipedLeft(position)
            repository.deleteSubById(subToRemove.id)
            reloadSubs()
        }
    }

    override fun onAddSubPressed() {
        createNavigator.goToCreateScreen()
        logger.addSubPressed()
    }

    override fun onSortBtnPressed() {
        sortNavigator.openSortScreen()
        logger.sortBtnClicked()
    }

    override fun onPeriodPressed() {
        val types = SubscriptionPeriodType.values()
        val index = (types.indexOf(sharedPrefs.selectedSubsPeriod) + 1).rem(types.size)
        sharedPrefs.selectedSubsPeriod = types[index]
        val price = SubscriptionPrice(calculateSubsPrice(), sharedPrefs.selectedCurrency)

        view.setSubsPeriod(sharedPrefs.selectedSubsPeriod)
        view.setSubsPrice(price)
        view.updateListPrices(sharedPrefs.selectedSubsPeriod)
        logger.onPeriodChangeClicked(types[index])
    }

}