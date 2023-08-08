package com.djavid.smartsubs.utils

import java.util.Currency

fun Currency.getSymbolString(): String {
    return when(currencyCode) {
        "RUB" -> "₽"
        else -> symbol
    }
}