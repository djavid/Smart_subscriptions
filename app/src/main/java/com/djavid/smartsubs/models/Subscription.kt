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

fun Context.getSubPeriodString(type: SubscriptionPeriodType) = when (type) {
    SubscriptionPeriodType.DAY -> getString(R.string.title_day)
    SubscriptionPeriodType.MONTH -> getString(R.string.title_month)
    SubscriptionPeriodType.YEAR -> getString(R.string.title_year)
}