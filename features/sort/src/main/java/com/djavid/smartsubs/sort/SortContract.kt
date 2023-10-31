package com.djavid.smartsubs.sort

import com.djavid.smartsubs.common.models.SortBy

interface SortContract {

    enum class ScreenType {
        SORT_BY, SORT
    }

    interface Presenter {
        fun init(type: ScreenType)
        fun onSortItemClicked(item: SortBy)
        fun onAscSortClicked()
        fun onDescSortClicked()
        fun onSortByClicked()
    }

    interface View {
        fun init(presenter: Presenter)
        fun showSortByList(items: List<SortBy>)
        fun showSortTitle(show: Boolean)
        fun setSortBySelection(sortBy: SortBy)
        fun showSortDelimiter(show: Boolean)
        fun setActiveSortType(asc: Boolean)
        fun finish()
    }

}