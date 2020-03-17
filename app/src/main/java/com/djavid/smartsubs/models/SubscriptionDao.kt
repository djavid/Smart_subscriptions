package com.djavid.smartsubs.models

import org.joda.time.DateTime

data class SubscriptionDao(
    val id: Long,
    val title: String,
    val price: Double,
    val currency: Currency,
    val period: SubscriptionPeriod,
    val periodStart: DateTime?,
    val comment: String?
)