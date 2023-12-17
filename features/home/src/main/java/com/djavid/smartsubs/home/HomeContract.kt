package com.djavid.smartsubs.home

import com.djavid.smartsubs.common.domain.SubscriptionUIModel
import com.djavid.smartsubs.common.domain.SubscriptionPeriodType
import com.djavid.smartsubs.common.domain.SubscriptionPrice

data class HomeState(
    val subsList: List<SubscriptionUIModel> = listOf(),
    val pricePeriod: SubscriptionPeriodType,
    val price: SubscriptionPrice,
    val isProgress: Boolean = false
)

sealed class HomeSideEffect {
    data object SlidePanelToTop : HomeSideEffect()
}