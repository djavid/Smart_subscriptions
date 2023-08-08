package com.djavid.smartsubs.analytics

import android.content.Context
import android.os.Bundle
import com.djavid.smartsubs.BuildConfig
import com.djavid.smartsubs.models.*
import com.djavid.smartsubs.utils.EventParam
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseLogger(
    context: Context
) {

    private val analytics = FirebaseAnalytics.getInstance(context)

    init {
        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)

        FirebaseInstallations.getInstance().id.addOnCompleteListener {
            if (it.isSuccessful) {
                analytics.setUserId(it.result)
            }
        }
    }

    suspend fun onActivateNotifClicked(notif: Notification) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(notif))
        }
        analytics.logEvent(Event.ACTIVATE_NOTIF_CLICKED.value, bundle)
    }

    suspend fun onNotifEdited(notif: Notification) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(notif))
        }
        analytics.logEvent(Event.NOTIF_EDITED.value, bundle)
    }

    suspend fun onNotifCreated(notif: Notification) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(notif))
        }
        analytics.logEvent(Event.NOTIF_CREATED.value, bundle)
    }

    suspend fun onNotifCheckClicked(model: Notification, checked: Boolean) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(model))
            putBoolean(EventParam.CHECKBOX_CHANGE.value, checked)
        }
        analytics.logEvent(Event.NOTIF_CHECK_CHANGED.value, bundle)
    }

    suspend fun onEditNotifClicked(model: Notification) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(model))
        }
        analytics.logEvent(Event.EDIT_NOTIF_CLICKED.value, bundle)
    }

    suspend fun onAddNotifClicked() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.ADD_NOTIF_CLICKED.value, Bundle())
    }

    suspend fun onNotifsClicked() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.SHOW_NOTIFS_CLICKED.value, Bundle())
    }

    suspend fun onSubEditClicked() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.EDIT_SUB_CLICKED.value, Bundle())
    }

    suspend fun onSortByChanged(sortBy: SortBy) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sortBy))
        }
        analytics.logEvent(Event.SORT_BY_CHANGED.value, bundle)
    }

    suspend fun onSortTypeChanged(sortType: SortType) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sortType))
        }
        analytics.logEvent(Event.SORT_TYPE_CHANGED.value, bundle)
    }

    suspend fun addSubPressed() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.ADD_SUB_CLICKED.value, Bundle())
    }

    suspend fun onPeriodChangeClicked(period: SubscriptionPeriodType) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(period))
        }
        analytics.logEvent(Event.PERIOD_CHANGE_CLICKED.value, bundle)
    }

    suspend fun subItemClicked(sub: Subscription) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sub))
        }
        analytics.logEvent(Event.SUB_ITEM_CLICKED.value, bundle)
    }

    suspend fun onSubItemSwipedLeft(position: Int) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putInt(EventParam.POSITION.value, position)
        }
        analytics.logEvent(Event.SUB_SWIPED_LEFT.value, bundle)
    }

    suspend fun subDelete(sub: Subscription) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sub))
        }
        analytics.logEvent(Event.SUB_DELETED.value, bundle)
    }

    suspend fun sortBtnClicked() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.SORT_BTN_CLICKED.value, Bundle())
    }

    suspend fun subCreated(sub: SubscriptionDao) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sub))
        }
        analytics.logEvent(Event.SUB_CREATED.value, bundle)
    }

    suspend fun subEdited(sub: SubscriptionDao) = withContext(Dispatchers.IO) {
        val bundle = Bundle().apply {
            putString(EventParam.JSON.value, Gson().toJson(sub))
        }
        analytics.logEvent(Event.SUB_EDITED.value, bundle)
    }

    suspend fun subscribeTgShown() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.SUBSCRIBE_TG_DIALOG_SHOWN.value, Bundle())
    }

    suspend fun subscribeTgClickedYes() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.SUBSCRIBE_TG_CLICKED_YES.value, Bundle())
    }

    suspend fun subscribeTgClickedNo() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.SUBSCRIBE_TG_CLICKED_NO.value, Bundle())
    }

    suspend fun inAppReviewShowTry() = withContext(Dispatchers.IO) {
        analytics.logEvent(Event.IN_APP_REVIEW_SHOW_TRY.value, Bundle())
    }

}