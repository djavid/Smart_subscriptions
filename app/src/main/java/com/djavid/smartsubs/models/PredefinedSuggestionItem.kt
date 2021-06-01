package com.djavid.smartsubs.models

data class PredefinedSuggestionItem(
    val subId: String,
    val title: String,
    val imageBytes: ByteArray,
    val abbreviations: List<String>
) {
    override fun toString(): String {
        return title
    }
}