package com.djavid.smartsubs.home

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.data.interactors.GetSelectedCurrencyInteractor
import com.djavid.smartsubs.data.interactors.GetSubsInteractor
import com.djavid.smartsubs.data.models.SubscriptionPeriodType
import com.djavid.smartsubs.data.models.SubscriptionPrice
import com.djavid.smartsubs.data.models.getPriceInPeriod
import com.djavid.smartsubs.data.storage.RealTimeRepository
import com.djavid.smartsubs.data.storage.SharedRepository
import com.djavid.smartsubs.utils.InAppReview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HomeInteractor(
    private val repository: RealTimeRepository,
    private val sharedRepository: SharedRepository,
    private val inAppReview: InAppReview,
    private val logger: FirebaseLogger,
    private val getSubsInteractor: GetSubsInteractor,
    private val getSelectedCurrencyInteractor: GetSelectedCurrencyInteractor,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getInitialState(): HomeState = withContext(ioDispatcher) {
        HomeState(
            pricePeriod = sharedRepository.selectedSubsPeriod,
            price = SubscriptionPrice(0.0, getSelectedCurrencyInteractor.execute() ),
            isProgress = true,
            subsList = emptyList()
        )
    }

    suspend fun onFirstOpen() = withContext(ioDispatcher) {
        sharedRepository.firstTimeOpened = false
        logger.onFirstTimeOpened()
    }

    suspend fun showInAppReview() {
        //todo do not show for rustore
        inAppReview.showInAppDialog()
    }

    suspend fun calculateSubsPrice(): SubscriptionPrice = withContext(ioDispatcher) {
        val pricePeriod = sharedRepository.selectedSubsPeriod
        val subsPriceSum = getSubsInteractor.execute()
            .filter { !it.isTrial() }.sumOf { it.getPriceInPeriod(pricePeriod) }

        SubscriptionPrice(subsPriceSum, getSelectedCurrencyInteractor.execute())
    }

    suspend fun removeSub(position: Int) = withContext(ioDispatcher) {
        val subToRemove = getSubsInteractor.execute()[position]
        logger.subDelete(subToRemove)
        logger.onSubItemSwipedLeft(position)
        repository.deleteSubById(subToRemove.id)
    }

    suspend fun togglePeriod(): SubscriptionPeriodType = withContext(ioDispatcher) {
        val types = SubscriptionPeriodType.values()
        val index = (types.indexOf(sharedRepository.selectedSubsPeriod) + 1).rem(types.size)
        val period = types[index]
        sharedRepository.selectedSubsPeriod = period

        period
    }

    suspend fun onSubItemClicked(id: String) = withContext(ioDispatcher) {
        getSubsInteractor.execute().find { it.id == id }?.let {
            logger.subItemClicked(it)
        }
    }

}