package com.djavid.smartsubs.utils

import android.content.Context
import android.os.Bundle
import com.djavid.smartsubs.BuildConfig
import com.djavid.smartsubs.models.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson

class FirebaseLogger(
    private val context: Context
) {

    private val analytics = FirebaseAnalytics.getInstance(context)

    init {
        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    fun onSortByChanged(sortBy: SortBy) {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "SORT_BY")
            putString("SORT_BY", sortBy.name)
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun onSortTypeChanged(sortType: SortType) {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "SORT_TYPE")
            putString("SORT_TYPE", sortType.name)
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun addSubPressed() {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "ADD_BTN")
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun onPeriodChangeClicked(period: SubscriptionPeriodType) {
        val bundle = Bundle().apply {
            putString("PRICE_PERIOD", period.name)
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun subItemClicked(sub: Subscription) {
        val bundle = Bundle().apply {
            putString("SUB_JSON", Gson().toJson(sub))
        }
        analytics.logEvent("SUB_CLICK", bundle)
    }

    fun onSubItemSwipedLeft(position: Int) {
        val bundle = Bundle().apply {
            putString("SUB_POSITION", position.toString())
        }
        analytics.logEvent("SUB_SWIPE_LEFT", bundle)
    }

    fun subDelete(sub: Subscription) {
        val bundle = Bundle().apply {
            putString("SUB_JSON", Gson().toJson(sub))
        }
        analytics.logEvent("SUB_DELETE", bundle)
    }

    fun sortBtnClicked() {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "SORT_BTN")
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun subCreated(sub: SubscriptionDao) {
        val bundle = Bundle().apply {
            putString("TITLE", sub.title)
            putString("PRICE", sub.price.toString())
            putString("CURRENCY", sub.currency.currencyCode)
            putString("PERIOD_TYPE", sub.period.type.name)
            putString("PERIOD_QUANTITY", sub.period.quantity.toString())
            putString("PAYMENT_DATE", sub.paymentDate?.toString("dd/MM/YYYY"))
            putString("CATEGORY", sub.category)
            putString("COMMENT", sub.comment)
        }
        analytics.logEvent("CREATE_SUB", bundle)
    }

}