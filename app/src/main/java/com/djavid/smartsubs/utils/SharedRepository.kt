package com.djavid.smartsubs.utils

import android.content.SharedPreferences

class SharedRepository(
    private val repository: SharedPreferences
) {

    companion object {
        const val VALUE_SELECTED_CURRENCY_CODE = "selected_currency_code"
    }

    var selectedCurrencyCode: String
        get() {
            return repository.getString(VALUE_SELECTED_CURRENCY_CODE, "RUB") ?: "RUB"
        }
        set(value) {
            repository.edit().putString(VALUE_SELECTED_CURRENCY_CODE, value).apply()
        }

}