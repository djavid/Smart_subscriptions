package com.djavid.smartsubs.models

data class Subscription(
    val id: Long,
    val title: String,
    val priceRubles: Double,
    val period: String,
    val periodStart: Long?,
    val periodEnd: Long?,
    val comment: String?
)