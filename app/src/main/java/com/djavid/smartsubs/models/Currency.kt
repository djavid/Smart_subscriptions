package com.djavid.smartsubs.models

import android.content.Context
import com.djavid.smartsubs.R

val supportedCurrencyCodes = listOf("RUB")

fun Context.getSymbolForCurrency(currency: java.util.Currency): String {
    return if (supportedCurrencyCodes.contains(currency.currencyCode)) {
        when (currency.currencyCode) {
            "RUB" -> getString(R.string.symbol_rub)
            else -> currency.displayName
        }
    } else {
        currency.displayName
    }
}