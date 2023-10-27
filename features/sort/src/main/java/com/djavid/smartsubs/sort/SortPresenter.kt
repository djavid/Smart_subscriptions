package com.djavid.smartsubs.sort

import com.djavid.common.BasePipeline
import com.djavid.smartsubs.models.SortBy
import com.djavid.smartsubs.models.SortType
import com.djavid.common.ACTION_REFRESH
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.common.KEY_SORT_BY
import com.djavid.data.storage.SharedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SortPresenter(
    private val view: SortContract.View,
    private val sharedRepository: com.djavid.data.storage.SharedRepository,
    private val sortNavigator: SortContract.Navigator,
    private val pipeline: com.djavid.common.BasePipeline<Pair<String, String>>,
    private val logger: FirebaseLogger,
    coroutineScope: CoroutineScope
) : SortContract.Presenter, CoroutineScope by coroutineScope {

    override fun init(type: SortContract.ScreenType) {
        view.init(this)

        when (type) {
            SortContract.ScreenType.SORT_BY -> showSortByScreen()
            SortContract.ScreenType.SORT -> {
                listenPipeline()
                showSortScreen()
            }
        }
    }

    private fun listenPipeline() {
        launch {
            pipeline.getFlow().onEach {
                when (it.first) {
                    com.djavid.common.KEY_SORT_BY -> {
                        loadSortBy()
                        pipeline.postValue(Pair(com.djavid.common.ACTION_REFRESH, ""))
                    }
                }
            }.collect()
        }
    }

    private fun showSortScreen() {
        view.showSortTitle(false)
        view.showSortDelimiter(true)

        loadSortBy()
        loadSortType()
    }

    private fun showSortByScreen() {
        view.showSortByList(SortBy.values().toList())
    }

    override fun onSortItemClicked(item: SortBy) {
        launch {
            sharedRepository.selectedSortBy = item
            pipeline.postValue(Pair(com.djavid.common.KEY_SORT_BY, item.name))
            logger.onSortByChanged(item)
            view.finish()
        }
    }

    override fun onSortByClicked() {
        sortNavigator.openSortByScreen()
    }

    override fun onAscSortClicked() {
        launch {
            if (sharedRepository.selectedSortType != SortType.ASC) {
                val sortType = SortType.ASC
                updateSortType(sortType)
                pipeline.postValue(Pair(com.djavid.common.ACTION_REFRESH, ""))
                logger.onSortTypeChanged(sortType)
            }
        }
    }

    override fun onDescSortClicked() {
        launch {
            if (sharedRepository.selectedSortType != SortType.DESC) {
                val sortType = SortType.DESC
                updateSortType(sortType)
                pipeline.postValue(Pair(com.djavid.common.ACTION_REFRESH, ""))
                logger.onSortTypeChanged(sortType)
            }
        }
    }

    private fun updateSortType(type: SortType) {
        sharedRepository.selectedSortType = type
        view.setActiveSortType(type == SortType.ASC)
    }

    private fun loadSortType() {
        view.setActiveSortType(sharedRepository.selectedSortType == SortType.ASC)
    }

    private fun loadSortBy() {
        view.setSortBySelection(sharedRepository.selectedSortBy)
    }

}