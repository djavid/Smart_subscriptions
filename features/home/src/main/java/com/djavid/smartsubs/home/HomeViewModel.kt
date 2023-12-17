package com.djavid.smartsubs.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.base.BasePipeline
import com.djavid.smartsubs.common.navigation.CreateNavigator
import com.djavid.smartsubs.common.navigation.SortNavigator
import com.djavid.smartsubs.common.navigation.SubscriptionNavigator
import com.djavid.smartsubs.data.interactors.GetSortedSubsInteractor
import com.djavid.smartsubs.data.storage.SharedRepository
import com.djavid.smartsubs.common.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(
    private val createNavigator: CreateNavigator,
    private val sortNavigator: SortNavigator,
    private val subNavigator: SubscriptionNavigator,
    private val sharedPrefs: SharedRepository,
    private val pipeline: BasePipeline<Pair<String, String>>,
    private val logger: FirebaseLogger,
    private val homeInteractor: HomeInteractor,
    private val getSortedSubsInteractor: GetSortedSubsInteractor,
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    override val container = container<HomeState, HomeSideEffect>(homeInteractor.getInitialState())

    init {
        viewModelScope.launch {
            if (sharedPrefs.firstTimeOpened) {
                homeInteractor.onFirstOpen()
            }

            listenPipeline()
            refreshSubs()
            intent { postSideEffect(HomeSideEffect.SlidePanelToTop) }
        }
    }

    fun onItemClick(id: String){
        viewModelScope.launch {
            subNavigator.goToSubscription(id)
            homeInteractor.onSubItemClicked(id)
        }
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

    private fun listenPipeline() = pipeline.getFlow().onEach {
        withContext(Dispatchers.IO) {
            when (it.first) {
                Constants.ACTION_REFRESH -> {
                    refreshSubs()

                    if (sharedPrefs.inAppReviewTimesShown <= 1) {
                        homeInteractor.showInAppReview()
                    }
                }
            }
        }
    }.launchIn(viewModelScope)

    private suspend fun refreshSubs() {
        val subs = getSortedSubsInteractor.execute()
        val price = homeInteractor.calculateSubsPrice()
        val pricePeriod = sharedPrefs.selectedSubsPeriod

        intent {
            reduce {
                state.copy(
                    isProgress = false,
                    price = price,
                    pricePeriod = pricePeriod,
                    subsList = subs
                )
            }
        }
    }
}