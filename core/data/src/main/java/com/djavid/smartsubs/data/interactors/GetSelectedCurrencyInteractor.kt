package com.djavid.smartsubs.data.interactors

import com.djavid.smartsubs.data.storage.SharedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency
import java.util.Locale

class GetSelectedCurrencyInteractor(
    private val sharedRepository: SharedRepository,
    private val getMostUsedCurrencyInteractor: GetMostUsedCurrencyInteractor
) {

    suspend fun execute(): Currency = withContext(Dispatchers.IO) {
        val currCode = sharedRepository.selectedCurrencyCode
        val firstTimeOpened = sharedRepository.firstTimeOpened

        if (currCode == null) {
            if (firstTimeOpened) {
                Currency.getInstance(Locale.getDefault())
            } else {
                getMostUsedCurrencyInteractor.execute()
                    ?: Currency.getInstance(Locale.getDefault())
            }
        } else {
            Currency.getInstance(currCode)
        }
    }

}