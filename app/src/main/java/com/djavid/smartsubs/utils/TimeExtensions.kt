package com.djavid.smartsubs.utils

import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import org.joda.time.DateTime

fun DateTime.addPeriod(period: SubscriptionPeriod): DateTime = when (period.type) {
    SubscriptionPeriodType.DAY -> plusDays(period.quantity)
    SubscriptionPeriodType.MONTH -> plusMonths(period.quantity)
    SubscriptionPeriodType.YEAR -> plusYears(period.quantity)
}

fun DateTime.minusPeriod(period: SubscriptionPeriod): DateTime = when (period.type) {
    SubscriptionPeriodType.DAY -> minusDays(period.quantity)
    SubscriptionPeriodType.MONTH -> minusMonths(period.quantity)
    SubscriptionPeriodType.YEAR -> minusYears(period.quantity)
}

fun DateTime.getFirstPeriodAfterNow(period: SubscriptionPeriod): DateTime {
    var date = this

    while (date.isAfterNow) {
        date = date.minusPeriod(period)
    }

    while (date.isBeforeNow) {
        date = date.addPeriod(period)
    }

    return date
}

fun DateTime.getFirstPeriodBeforeNow(period: SubscriptionPeriod): DateTime {
    var date = this

    while (date.isBeforeNow) {
        date = date.addPeriod(period)
    }

    while (date.isAfterNow) {
        date = date.minusPeriod(period)
    }

    return date
}