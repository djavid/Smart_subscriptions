package com.djavid.smartsubs.common.entity

data class PredefinedSubscriptionFirebaseEntity(
    val id: String = "",
    val title: String = "",
    val logoUrl: String = "",
    val description: String = "",
    val abbreviations: List<String> = emptyList()
)