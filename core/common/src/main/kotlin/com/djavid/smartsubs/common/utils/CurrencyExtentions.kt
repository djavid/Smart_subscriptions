package com.djavid.smartsubs.common.utils

import java.util.Currency
import java.util.Locale

fun Currency.getSymbolString(): String {
    return when(currencyCode) {
        "RUB" -> "₽"
        else -> symbol
    }
}

fun defaultCurrency(): Currency = Currency.getInstance(Locale.getDefault())