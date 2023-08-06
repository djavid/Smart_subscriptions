package com.djavid.smartsubs.models

enum class SupportedCurrencies {
    RUB, USD, EUR, GBP, JPY, CHF, CNY
}

sealed class Currencies(
    val currencyCode: String
) {
    sealed class RussianRouble(
        currencyCode: String = "RUB"
    ): Currencies(currencyCode) {

    }
}