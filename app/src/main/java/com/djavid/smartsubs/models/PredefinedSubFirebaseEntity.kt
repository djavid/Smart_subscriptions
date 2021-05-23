package com.djavid.smartsubs.models

data class PredefinedSubFirebaseEntity(
    val id: String = "",
    val title: String = "",
    val logoUrl: String = "",
    val description: String = "",
    val abbreviations: List<String> = emptyList()
)