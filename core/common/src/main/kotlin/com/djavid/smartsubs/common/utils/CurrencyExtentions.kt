package com.djavid.smartsubs.common.utils

import java.util.Currency
import java.util.Locale

fun Currency.getCurrencySymbol(): String {
    return when(currencyCode) {
        "RUB" -> "₽"
        "USD" -> "$"
        else -> symbol
    }
}

fun defaultCurrency(): Currency = Currency.getInstance(Locale.getDefault())