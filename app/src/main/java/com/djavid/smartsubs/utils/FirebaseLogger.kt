package com.djavid.smartsubs.utils

import android.content.Context
import android.os.Bundle
import com.djavid.smartsubs.BuildConfig
import com.djavid.smartsubs.models.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson

class FirebaseLogger(
    private val context: Context
) {

    private val analytics = FirebaseAnalytics.getInstance(context)

    init {
        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        analytics.setUserId(FirebaseInstanceId.getInstance().id)
    }

    fun onActivateNotifClicked(notif: Notification) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(notif))
        }
        analytics.logEvent(Event.ACTIVATE_NOTIF_CLICKED.value, bundle)
    }

    fun onNotifEdited(notif: Notification) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(notif))
        }
        analytics.logEvent(Event.NOTIF_EDITED.value, bundle)
    }

    fun onNotifCreated(notif: Notification) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(notif))
        }
        analytics.logEvent(Event.NOTIF_CREATED.value, bundle)
    }

    fun onNotifCheckClicked(model: Notification, checked: Boolean) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(model))
            putBoolean(EventParam.CHECKBOX_CHANGE.value, checked)
        }
        analytics.logEvent(Event.NOTIF_CHECK_CHANGED.value, bundle)
    }

    fun onEditNotifClicked(model: Notification) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(model))
        }
        analytics.logEvent(Event.EDIT_NOTIF_CLICKED.value, bundle)
    }

    fun onAddNotifClicked() {
        analytics.logEvent(Event.ADD_NOTIF_CLICKED.value, Bundle())
    }

    fun onNotifsClicked() {
        analytics.logEvent(Event.SHOW_NOTIFS_CLICKED.value, Bundle())
    }

    fun onSubEditClicked() {
        analytics.logEvent(Event.EDIT_SUB_CLICKED.value, Bundle())
    }

    fun onSortByChanged(sortBy: SortBy) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sortBy))
        }
        analytics.logEvent(Event.SORT_BY_CHANGED.value, bundle)
    }

    fun onSortTypeChanged(sortType: SortType) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sortType))
        }
        analytics.logEvent(Event.SORT_TYPE_CHANGED.value, bundle)
    }

    fun addSubPressed() {
        analytics.logEvent(Event.ADD_SUB_CLICKED.value, Bundle())
    }

    fun onPeriodChangeClicked(period: SubscriptionPeriodType) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(period))
        }
        analytics.logEvent(Event.PERIOD_CHANGE_CLICKED.value, bundle)
    }

    fun subItemClicked(sub: Subscription) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sub))
        }
        analytics.logEvent(Event.SUB_ITEM_CLICKED.value, bundle)
    }

    fun onSubItemSwipedLeft(position: Int) {
        val bundle = Bundle().apply {
            putInt(EventParam.POSITION.value, position)
        }
        analytics.logEvent(Event.SUB_SWIPED_LEFT.value, bundle)
    }

    fun subDelete(sub: Subscription) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sub))
        }
        analytics.logEvent(Event.SUB_DELETED.value, bundle)
    }

    fun sortBtnClicked() {
        analytics.logEvent(Event.SORT_BTN_CLICKED.value, Bundle())
    }

    fun subCreated(sub: SubscriptionDao) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sub))
        }
        analytics.logEvent(Event.SUB_CREATED.value, bundle)
    }

    fun subEdited(sub: SubscriptionDao) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sub))
        }
        analytics.logEvent(Event.SUB_EDITED.value, bundle)
    }

}