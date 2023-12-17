package com.djavid.ui

import android.content.Context
import com.djavid.core.ui.R
import com.djavid.smartsubs.common.domain.SortBy
import com.djavid.smartsubs.common.domain.SubscriptionPeriodType

fun Context.getSubPeriodString(type: SubscriptionPeriodType, quantity: Int = 1) = when (type) {
    SubscriptionPeriodType.DAY -> resources.getQuantityString(R.plurals.plural_day, quantity)
    SubscriptionPeriodType.WEEK -> resources.getQuantityString(R.plurals.plural_week, quantity)
    SubscriptionPeriodType.MONTH -> resources.getQuantityString(R.plurals.plural_month, quantity)
    SubscriptionPeriodType.YEAR -> resources.getQuantityString(R.plurals.plural_year, quantity)
}

fun SortBy.getTitle(context: Context): String {
    return when (this) {
        SortBy.TITLE -> context.getString(R.string.sort_by_title)
        SortBy.PRICE -> context.getString(R.string.sort_by_price)
        SortBy.DAYS_UNTIL -> context.getString(R.string.sort_by_days_left)
        SortBy.CREATION_DATE -> context.getString(R.string.sort_by_creation_date)
    }
}