package com.djavid.smartsubs.home

import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.SubscriptionPrice

data class HomeState(
    val subsList: List<Subscription> = listOf(),
    val pricePeriod: SubscriptionPeriodType,
    val price: SubscriptionPrice,
    val isProgress: Boolean = false
)

sealed class HomeSideEffect {
    data object SlidePanelToTop : HomeSideEffect()
}

interface HomeNavigator {
    fun goToHome()
}