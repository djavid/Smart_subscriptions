package com.djavid.smartsubs.utils

import android.content.SharedPreferences
import com.djavid.smartsubs.models.SubscriptionPeriodType
import java.util.*

class SharedRepository(
    private val repository: SharedPreferences
) {

    companion object {
        const val PREF_SELECTED_CURRENCY_CODE = "selected_currency_code"
        const val PREF_SELECTED_SUBS_PERIOD = "selected_subs_period"
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

}