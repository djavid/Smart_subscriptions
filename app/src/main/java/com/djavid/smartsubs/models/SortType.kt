package com.djavid.smartsubs.models

import android.content.Context
import com.djavid.smartsubs.R

enum class SortType {
    ASC, DESC
}

enum class SortBy {
    TITLE, PRICE, DAYS_UNTIL, CREATION_DATE;

    fun getTitle(context: Context): String {
        return when (this) {
            TITLE -> context.getString(R.string.sort_by_title)
            PRICE -> context.getString(R.string.sort_by_price)
            DAYS_UNTIL -> context.getString(R.string.sort_by_days_left)
            CREATION_DATE -> context.getString(R.string.sort_by_creation_date)
        }
    }
}