package com.djavid.smartsubs.common.models

import com.djavid.smartsubs.common.utils.localNow
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

data class SubscriptionDao(
    val id: String,
    val creationDate: DateTime,
    val title: String,
    val price: Double,
    val currency: Currency,
    val period: SubscriptionPeriod,
    val paymentDate: LocalDate?,
    val category: String?,
    val comment: String?,
    val trialPaymentDate: LocalDate?,
    val predefinedSubId: String?
) {
    fun hasTrialEnded(): Boolean = trialPaymentDate != null && localNow().isAfter(trialPaymentDate)

}