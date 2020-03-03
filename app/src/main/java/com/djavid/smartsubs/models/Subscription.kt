package com.djavid.smartsubs.models

import android.content.Context
import com.djavid.smartsubs.R
import com.djavid.smartsubs.utils.Instant

data class Subscription(
    val title: String,
    val price: Double,
    val period: SubscriptionPeriod,
    val periodEnd: Instant,
    val daysLeft: Int,
    val progress: Int //[0, 100]
)

data class SubscriptionPeriod(
    val type: SubscriptionPeriodType,
    val quantity: Int = 0
)

enum class SubscriptionPeriodType {
    DAY, MONTH, YEAR
}

fun Context.getSubPeriodString(type: SubscriptionPeriodType) = when (type) {
    SubscriptionPeriodType.DAY -> getString(R.string.title_day)
    SubscriptionPeriodType.MONTH -> getString(R.string.title_month)
    SubscriptionPeriodType.YEAR -> getString(R.string.title_year)
}