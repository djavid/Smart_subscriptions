package com.djavid.smartsubs.models

import org.joda.time.LocalDate
import java.util.*

data class SubscriptionDao(
    val id: Long,
    val title: String,
    val price: Double,
    val currency: Currency,
    val period: SubscriptionPeriod,
    val paymentDate: LocalDate?,
    val category: String?,
    val comment: String?,
    val trialPaymentDate: LocalDate?
)