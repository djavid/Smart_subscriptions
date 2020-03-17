package com.djavid.smartsubs.models

import android.content.Context
import com.djavid.smartsubs.R

enum class Currency(title: String) {
    RUB("rub")
}

fun Context.getSymbolForCurrency(currency: Currency) = when (currency) {
    Currency.RUB -> getString(R.string.symbol_rub)
}