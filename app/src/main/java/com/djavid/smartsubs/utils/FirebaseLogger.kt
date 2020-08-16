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

    fun onActivateNotifClicked() {
        val bundle = Bundle().apply {
            putString("ACTION_NAME", "ACTIVATE_NOTIF")
        }
        analytics.logEvent("ACTION", bundle)
    }

    fun onNotifEdited(notif: Notification) {
        val bundle = Bundle().apply {
            putString("ACTION_NAME", "NOTIF_EDIT")
            putString("NOTIF_JSON", Gson().toJson(notif))
        }
        analytics.logEvent("ACTION", bundle)
    }

    fun onNotifCreated(notif: Notification) {
        val bundle = Bundle().apply {
            putString("ACTION_NAME", "NOTIF_CREATE")
            putString("NOTIF_JSON", Gson().toJson(notif))
        }
        analytics.logEvent("ACTION", bundle)
    }

    fun onNotifCheckClicked(model: Notification, checked: Boolean) {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "NOTIF_CHECK_CHANGE")
            putString("NOTIF_JSON", Gson().toJson(model))
            putString("NOTIF_CHECKED", checked.toString())
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun onEditNotifClicked(model: Notification) {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "EDIT_NOTIF")
            putString("NOTIF_JSON", Gson().toJson(model))
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun onAddNotifClicked() {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "ADD_NOTIF")
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun onNotifsClicked() {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "NOTIFS")
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun onSubEditClicked() {
        val bundle = Bundle().apply {
            putString("BUTTON_NAME", "SUB_EDIT")
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
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
            putString("BUTTON_NAME", "SUB_CLICK")
            putString("SUB_JSON", Gson().toJson(sub))
        }
        analytics.logEvent("BUTTON_CLICK", bundle)
    }

    fun onSubItemSwipedLeft(position: Int) {
        val bundle = Bundle().apply {
            putString("ACTION_NAME", "SUB_SWIPE_LEFT")
            putString("SUB_POSITION", position.toString())
        }
        analytics.logEvent("ACTION", bundle)
    }

    fun subDelete(sub: Subscription) {
        val bundle = Bundle().apply {
            putString("ACTION_NAME", "SUB_DELETE")
            putString("SUB_JSON", Gson().toJson(sub))
        }
        analytics.logEvent("ACTION", bundle)
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