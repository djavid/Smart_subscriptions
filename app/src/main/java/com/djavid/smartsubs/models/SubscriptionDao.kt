package com.djavid.smartsubs.models

import org.joda.time.DateTime
import java.util.Currency

data class SubscriptionDao(
    val id: Long,
    val title: String,
    val price: Double,
    val currency: Currency,
    val period: SubscriptionPeriod,
    val paymentDate: DateTime?,
    val comment: String?
)