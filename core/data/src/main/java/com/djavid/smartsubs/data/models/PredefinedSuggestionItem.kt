package com.djavid.smartsubs.data.models

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PredefinedSuggestionItem

        if (subId != other.subId) return false
        if (title != other.title) return false
        if (!imageBytes.contentEquals(other.imageBytes)) return false
        if (abbreviations != other.abbreviations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = subId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + imageBytes.contentHashCode()
        result = 31 * result + abbreviations.hashCode()
        return result
    }
}