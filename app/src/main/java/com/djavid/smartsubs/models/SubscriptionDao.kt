package com.djavid.smartsubs.models

import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

data class SubscriptionDao(
    val id: Long,
    val creationDate: DateTime,
    val title: String,
    val price: Double,
    val currency: Currency,
    val period: SubscriptionPeriod,
    val paymentDate: LocalDate?,
    val category: String?,
    val comment: String?,
    val trialPaymentDate: LocalDate?,
    val isLoaded: Boolean,
    val iconUrl: String?
)