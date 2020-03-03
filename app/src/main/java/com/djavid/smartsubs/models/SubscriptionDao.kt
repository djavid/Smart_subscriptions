package com.djavid.smartsubs.models

data class SubscriptionDao(
    val id: Long,
    val title: String,
    val priceRubles: Double,
    val period: String,
    val periodStart: Long?,
    val periodEnd: Long?,
    val comment: String?
)