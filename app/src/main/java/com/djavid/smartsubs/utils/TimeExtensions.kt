package com.djavid.smartsubs.utils

import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import org.joda.time.DateTime

fun DateTime.addPeriod(period: SubscriptionPeriod): DateTime = when (period.type) {
    SubscriptionPeriodType.DAY -> plusDays(period.quantity)
    SubscriptionPeriodType.MONTH -> plusMonths(period.quantity)
    SubscriptionPeriodType.YEAR -> plusYears(period.quantity)
}