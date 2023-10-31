package com.djavid.smartsubs.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.common.CreateNavigator
import com.djavid.smartsubs.common.SortNavigator
import com.djavid.smartsubs.common.SubscriptionNavigator
import com.djavid.smartsubs.create.CreateContract
import com.djavid.smartsubs.data.interactors.GetSortedSubsInteractor
import com.djavid.smartsubs.data.storage.RealTimeRepository
import com.djavid.smartsubs.data.storage.SharedRepository
import com.djavid.smartsubs.sort.SortContract
import com.djavid.smartsubs.subscription.SubscriptionContract
import com.djavid.smartsubs.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(
    private val repository: RealTimeRepository,
    private val createNavigator: CreateNavigator,
    private val sortNavigator: SortNavigator,
    private val subNavigator: SubscriptionNavigator,
    private val sharedPrefs: SharedRepository,
    private val pipeline: BasePipeline<Pair<String, String>>,
    private val logger: FirebaseLogger,
    private val homeInteractor: HomeInteractor,
    private val getSortedSubsInteractor: GetSortedSubsInteractor,
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    override val container = container<HomeState, HomeSideEffect>(
        runBlocking {
            homeInteractor.getInitialState()
        }
    )

    init {
        intent { postSideEffect(HomeSideEffect.SlidePanelToTop) }

        viewModelScope.launch {
            listenPipeline()

            if (sharedPrefs.firstTimeOpened) {
                homeInteractor.onFirstOpen()
            }
        }
    }

    fun onItemClick(id: String) = viewModelScope.launch {
        subNavigator.goToSubscription(id)
        homeInteractor.onSubItemClicked(id)
    }

    fun onAddSubPressed() = viewModelScope.launch {
        createNavigator.goToCreateScreen()
        logger.addSubPressed()
    }

    fun onSortBtnPressed() = viewModelScope.launch {
        sortNavigator.openSortScreen()
        logger.sortBtnClicked()
    }

    fun onPeriodPressed() = viewModelScope.launch {
        val period = homeInteractor.togglePeriod()
        val price = homeInteractor.calculateSubsPrice()

        intent { reduce { state.copy(pricePeriod = period, price = price) } }
        logger.onPeriodChangeClicked(period)
    }

    fun onRefreshAction() = viewModelScope.launch {
        refreshSubs()
    }

    fun onResume() = viewModelScope.launch {
        refreshSubs()
    }

    private fun listenPipeline() = viewModelScope.launch(Dispatchers.IO) {
        pipeline.getFlow().collect {
            when (it.first) {
                Constants.ACTION_REFRESH -> {
                    refreshSubs()

                    if (sharedPrefs.inAppReviewTimesShown < 2) {
                        homeInteractor.showInAppReview()
                    }
                }
            }
        }
    }

    private suspend fun refreshSubs() = intent {
        repository.updateTrialSubs()
        val subs = getSortedSubsInteractor.execute()
        val price = homeInteractor.calculateSubsPrice()
        val pricePeriod = sharedPrefs.selectedSubsPeriod

        reduce {
            state.copy(
                isProgress = false, price = price, pricePeriod = pricePeriod, subsList = subs
            )
        }
    }

}