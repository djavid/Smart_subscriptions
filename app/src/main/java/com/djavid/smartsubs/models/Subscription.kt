package com.djavid.smartsubs.models

import android.content.Context
import com.djavid.smartsubs.R
import com.djavid.smartsubs.utils.DAYS_IN_WEEK
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

data class Subscription(
    val id: String,
    val title: String,
    val price: SubscriptionPrice,
    val period: SubscriptionPeriod,
    val progress: SubscriptionProgress?,
    val category: String?,
    val overallSpent: Double?,
    val comment: String?,
    val trialPaymentDate: LocalDate?,
    val logoBytes: ByteArray?
) {
    fun isTrial() = trialPaymentDate != null
}

data class SubscriptionPrice(
    val value: Double,
    val currency: Currency
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
    DAY, WEEK, MONTH, YEAR
}

fun Context.getSubPeriodString(type: SubscriptionPeriodType, quantity: Int = 1) = when (type) {
    SubscriptionPeriodType.DAY -> resources.getQuantityString(R.plurals.plural_day, quantity)
    SubscriptionPeriodType.WEEK -> resources.getQuantityString(R.plurals.plural_week, quantity)
    SubscriptionPeriodType.MONTH -> resources.getQuantityString(R.plurals.plural_month, quantity)
    SubscriptionPeriodType.YEAR -> resources.getQuantityString(R.plurals.plural_year, quantity)
}

fun Subscription.getPriceInPeriod(pricePeriod: SubscriptionPeriodType): Double {
    val daysInWeek = DAYS_IN_WEEK
    val daysInMonth = DateTime().dayOfMonth().maximumValue
    val daysInYear = DateTime().dayOfYear().maximumValue
    val monthsInYear = DateTime().monthOfYear().maximumValue

    val priceForOne = price.value / period.quantity

    return when (pricePeriod) { //todo check
        SubscriptionPeriodType.DAY -> when (period.type) {
            SubscriptionPeriodType.DAY -> priceForOne
            SubscriptionPeriodType.WEEK -> priceForOne / daysInWeek
            SubscriptionPeriodType.MONTH -> priceForOne / daysInMonth
            SubscriptionPeriodType.YEAR -> priceForOne / daysInYear
        }
        SubscriptionPeriodType.WEEK -> when (period.type) {
            SubscriptionPeriodType.DAY -> priceForOne * daysInWeek
            SubscriptionPeriodType.WEEK -> priceForOne
            SubscriptionPeriodType.MONTH -> (priceForOne / daysInMonth) * daysInWeek
            SubscriptionPeriodType.YEAR -> priceForOne / daysInYear * daysInWeek
        }
        SubscriptionPeriodType.MONTH -> when (period.type) {
            SubscriptionPeriodType.DAY -> priceForOne * daysInMonth
            SubscriptionPeriodType.WEEK -> (daysInMonth / daysInWeek) * priceForOne
            SubscriptionPeriodType.MONTH -> priceForOne
            SubscriptionPeriodType.YEAR -> priceForOne / monthsInYear
        }
        SubscriptionPeriodType.YEAR -> when (period.type) {
            SubscriptionPeriodType.DAY -> priceForOne * daysInYear
            SubscriptionPeriodType.WEEK -> (daysInYear / daysInWeek) * priceForOne
            SubscriptionPeriodType.MONTH -> priceForOne * monthsInYear
            SubscriptionPeriodType.YEAR -> priceForOne
        }
    }
}