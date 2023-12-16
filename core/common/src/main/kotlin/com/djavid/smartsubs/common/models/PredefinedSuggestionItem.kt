package com.djavid.smartsubs.common.models

import java.io.Serializable

data class PredefinedSuggestionItem(
    val subId: String,
    val title: String,
    val logoUrl: String,
    val abbreviations: List<String>
): Serializable {
    override fun toString(): String {
        return title
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PredefinedSuggestionItem

        if (subId != other.subId) return false
        if (title != other.title) return false
        if (logoUrl != other.logoUrl) return false
        return abbreviations == other.abbreviations
    }

    override fun hashCode(): Int {
        var result = subId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + logoUrl.hashCode()
        result = 31 * result + abbreviations.hashCode()
        return result
    }
}