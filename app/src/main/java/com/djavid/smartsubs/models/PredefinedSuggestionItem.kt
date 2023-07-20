package com.djavid.smartsubs.models

import java.io.Serializable

data class PredefinedSuggestionItem(
    val subId: String,
    val title: String,
    val imageBytes: ByteArray,
    val abbreviations: List<String>
): Serializable {
    override fun toString(): String {
        return title
    }
}