package com.djavid.smartsubs.data.interactors

import com.djavid.smartsubs.data.storage.RealTimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency

class GetMostUsedCurrencyInteractor(
    private val repository: RealTimeRepository
) {

    suspend fun execute(): Currency? = withContext(Dispatchers.IO) {
        val currCode = repository.getSubs(true)
            .groupBy { it.currency.currencyCode }
            .maxByOrNull { it.value.size }
            ?.key

        currCode?.let { Currency.getInstance(it) }
    }


}