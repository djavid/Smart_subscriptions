package com.djavid.smartsubs.data.interactors

import com.djavid.smartsubs.data.models.Subscription
import com.djavid.smartsubs.data.models.SortBy
import com.djavid.smartsubs.data.models.SortType
import com.djavid.smartsubs.data.models.getPriceInPeriod
import com.djavid.smartsubs.data.storage.SharedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class GetSortedSubsInteractor(
    private val sharedRepository: SharedRepository,
    private val getSubsInteractor: GetSubsInteractor
) {

    suspend fun execute(): List<Subscription> = withContext(Dispatchers.IO) {
        val subs = getSubsInteractor.execute()
        val asc = sharedRepository.selectedSortType == SortType.ASC

        if (asc) {
            when (sharedRepository.selectedSortBy) {
                SortBy.TITLE -> subs.sortedBy { it.title.lowercase(Locale.getDefault()) }
                SortBy.PRICE -> subs.sortedBy { it.getPriceInPeriod(sharedRepository.selectedSubsPeriod) }
                SortBy.DAYS_UNTIL -> subs.sortedBy { it.progress?.daysLeft }
                SortBy.CREATION_DATE -> subs.sortedBy { it.id }
            }
        } else {
            when (sharedRepository.selectedSortBy) {
                SortBy.TITLE -> subs.sortedByDescending { it.title.lowercase(Locale.getDefault()) }
                SortBy.PRICE -> subs.sortedByDescending { it.getPriceInPeriod(sharedRepository.selectedSubsPeriod) }
                SortBy.DAYS_UNTIL -> subs.sortedByDescending { it.progress?.daysLeft }
                SortBy.CREATION_DATE -> subs.sortedByDescending { it.id }
            }
        }
    }
}