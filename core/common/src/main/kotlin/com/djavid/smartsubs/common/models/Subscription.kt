package com.djavid.smartsubs.common.models

import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

const val DAYS_IN_WEEK = 7

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subscription

        if (id != other.id) return false
        if (title != other.title) return false
        if (price != other.price) return false
        if (period != other.period) return false
        if (progress != other.progress) return false
        if (category != other.category) return false
        if (overallSpent != other.overallSpent) return false
        if (comment != other.comment) return false
        if (trialPaymentDate != other.trialPaymentDate) return false
        if (logoBytes != null) {
            if (other.logoBytes == null) return false
            if (!logoBytes.contentEquals(other.logoBytes)) return false
        } else if (other.logoBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + period.hashCode()
        result = 31 * result + (progress?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (overallSpent?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + (trialPaymentDate?.hashCode() ?: 0)
        result = 31 * result + (logoBytes?.contentHashCode() ?: 0)
        return result
    }
}

data class SubscriptionPrice(
    val value: Double,
    val currency: Currency
)

data class SubscriptionProgress(
    val daysLeft: Int,
    val value: Double //[0, 1]
)

data class SubscriptionPeriod(
    val type: SubscriptionPeriodType,
    val quantity: Int
)

enum class SubscriptionPeriodType {
    DAY, WEEK, MONTH, YEAR
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