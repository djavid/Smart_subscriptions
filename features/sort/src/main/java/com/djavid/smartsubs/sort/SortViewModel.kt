package com.djavid.smartsubs.sort

import androidx.lifecycle.LiveData
import com.djavid.smartsubs.common.domain.SortBy
import com.djavid.smartsubs.common.domain.SortType

interface SortViewModel {
    val sortByValues: LiveData<List<SortBy>>
    val selectedSortType: LiveData<SortType>
    val selectedSortBy: LiveData<SortBy>

    fun selectSortByItem(item: SortBy)
    fun selectSortType(sortType: SortType)
    fun openSortByScreen()
}