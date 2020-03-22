package com.djavid.smartsubs.models

import android.content.Context
import com.djavid.smartsubs.R
import java.util.Currency

data class Subscription(
    val id: Long,
    val title: String,
    val price: Double,
    val currency: Currency,
    val period: SubscriptionPeriod,
    val progress: SubscriptionProgress?
)

data class SubscriptionProgress(
    val daysLeft: Int,
    val progress: Double //[0, 1]
)

data class SubscriptionPeriod(
    val type: SubscriptionPeriodType,
    val quantity: Int
)

enum class SubscriptionPeriodType {
    DAY, MONTH, YEAR
}

fun Context.getSubPeriodString(type: SubscriptionPeriodType, quantity: Int = 1) = when (type) {
    SubscriptionPeriodType.DAY -> resources.getQuantityString(R.plurals.plural_day, quantity)
    SubscriptionPeriodType.MONTH -> resources.getQuantityString(R.plurals.plural_month, quantity)
    SubscriptionPeriodType.YEAR -> resources.getQuantityString(R.plurals.plural_year, quantity)
}