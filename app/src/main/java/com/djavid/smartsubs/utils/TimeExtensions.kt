package com.djavid.smartsubs.utils

import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.*

fun LocalDate.addPeriod(period: SubscriptionPeriod): LocalDate = when (period.type) {
    SubscriptionPeriodType.DAY -> plusDays(period.quantity)
    SubscriptionPeriodType.MONTH -> {
        if (dayOfMonth == dayOfMonth().maximumValue) {
            plusMonths(period.quantity).dayOfMonth().withMaximumValue()
        } else {
            plusMonths(period.quantity)
        }
    }
    SubscriptionPeriodType.YEAR -> plusYears(period.quantity)
}

fun LocalDate.minusPeriod(period: SubscriptionPeriod): LocalDate = when (period.type) {
    SubscriptionPeriodType.DAY -> minusDays(period.quantity)
    SubscriptionPeriodType.MONTH -> minusMonths(period.quantity)
    SubscriptionPeriodType.YEAR -> minusYears(period.quantity)
}

fun LocalDate.getFirstPeriodAfterNow(period: SubscriptionPeriod): LocalDate {
    var date = this
    val dateNow = LocalDate.now(DateTimeZone.forTimeZone(TimeZone.getDefault()))

    while (date.isAfter(dateNow)) {
        date = date.minusPeriod(period)
    }

    while (date.isBefore(dateNow)) {
        date = date.addPeriod(period)
    }

    return date
}

fun LocalDate.getFirstPeriodBeforeNow(period: SubscriptionPeriod): LocalDate {
    var date = this
    val dateNow = LocalDate.now(DateTimeZone.forTimeZone(TimeZone.getDefault()))

    while (date.isBefore(dateNow)) {
        date = date.addPeriod(period)
    }

    while (date.isAfter(dateNow)) {
        date = date.minusPeriod(period)
    }

    return date
}

fun LocalDate.getPeriodsCountBeforeNow(period: SubscriptionPeriod): Int {
    val dateNow = LocalDate.now(DateTimeZone.forTimeZone(TimeZone.getDefault()))

    return if (this.isAfter(dateNow))
        0
    else {
        var date = this
        var count = 0

        while (date.isBefore(dateNow)) {
            date = date.addPeriod(period)
            count++
        }

        count - 1
    }
}