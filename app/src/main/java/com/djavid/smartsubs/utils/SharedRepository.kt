package com.djavid.smartsubs.utils

import android.content.SharedPreferences
import com.djavid.smartsubs.models.SortBy
import com.djavid.smartsubs.models.SortType
import com.djavid.smartsubs.models.SubscriptionPeriodType
import java.util.*

class SharedRepository(
    private val repository: SharedPreferences
) {

    companion object {
        const val PREF_SELECTED_CURRENCY_CODE = "selected_currency_code"
        const val PREF_SELECTED_SUBS_PERIOD = "selected_subs_period"
        const val PREF_LAST_NOTIF_ID = "last_notif_id"
        const val PREF_SELECTED_SORT_TYPE = "selected_sort_type"
        const val PREF_SELECTED_SORT_BY = "selected_sort_by"
        const val PREF_TG_DIALOG_TIMES_SHOWN = "tg_dialog_times_shown"
        const val PREF_TG_DIALOG_YES_TIMES_CLICKED = "tg_dialog_yes_times_clicked"
        const val PREF_FIRST_TIME_OPENED = "first_time_opened"
    }

    var selectedCurrency: Currency
        get() {
            val currCode = repository.getString(PREF_SELECTED_CURRENCY_CODE, null)
            return Currency.getInstance(currCode ?: "RUB")
        }
        set(value) {
            repository.edit().putString(PREF_SELECTED_CURRENCY_CODE, value.currencyCode).apply()
        }

    var selectedSubsPeriod: SubscriptionPeriodType
        get() {
            return repository.getString(PREF_SELECTED_SUBS_PERIOD, null)?.let {
                SubscriptionPeriodType.valueOf(it)
            } ?: SubscriptionPeriodType.MONTH
        }
        set(value) {
            repository.edit().putString(PREF_SELECTED_SUBS_PERIOD, value.name).apply()
        }

    var selectedSortType: SortType
        get() {
            return repository.getString(PREF_SELECTED_SORT_TYPE, null)?.let {
                SortType.valueOf(it)
            } ?: SortType.DESC
        }
        set(value) {
            repository.edit().putString(PREF_SELECTED_SORT_TYPE, value.name).apply()
        }

    var selectedSortBy: SortBy
        get() {
            return repository.getString(PREF_SELECTED_SORT_BY, null)?.let {
                SortBy.valueOf(it)
            } ?: SortBy.CREATION_DATE
        }
        set(value) {
            repository.edit().putString(PREF_SELECTED_SORT_BY, value.name).apply()
        }

    fun nextNotifId(): Int {
        var nextId = repository.getInt(PREF_LAST_NOTIF_ID, 0) + 1

        nextId = if (nextId == Integer.MAX_VALUE) 0 else nextId
        repository.edit().putInt(PREF_LAST_NOTIF_ID, nextId).apply()

        return nextId
    }

    var tgDialogTimesShown: Int
        get() {
            return repository.getInt(PREF_TG_DIALOG_TIMES_SHOWN, 0)
        }
        set(value) {
            repository.edit().putInt(PREF_TG_DIALOG_TIMES_SHOWN, value).apply()
        }

    var tgDialogTimesYesClicked: Int
        get() {
            return repository.getInt(PREF_TG_DIALOG_YES_TIMES_CLICKED, 0)
        }
        set(value) {
            repository.edit().putInt(PREF_TG_DIALOG_YES_TIMES_CLICKED, value).apply()
        }

    var firstTimeOpened: Boolean
        get() {
            return repository.getBoolean(PREF_FIRST_TIME_OPENED, true)
        }
        set(value) {
            repository.edit().putBoolean(PREF_FIRST_TIME_OPENED, value).apply()
        }

}