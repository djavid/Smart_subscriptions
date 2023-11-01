package com.djavid.smartsubs.common.models

import com.djavid.smartsubs.common.utils.localNow
import com.djavid.smartsubs.common.utils.toLocalDate

data class SubscriptionFirebaseEntity(
    val id: String = "",
    val creationDate: Long = 0,
    val title: String = "",
    val price: Double = 0.0,
    val currencyCode: String = "",
    val periodQuantity: Long = 0,
    val period: String = "",
    val paymentDate: Long? = null,
    val category: String? = null,
    val comment: String? = null,
    val trialPaymentDate: Long? = null,
    val loaded: Boolean = false,
    val predefinedSubId: String? = null
) {

    fun hasTrialEnded(): Boolean =
        trialPaymentDate != null && trialPaymentDate.let { localNow().isAfter(it.toLocalDate()) }

}