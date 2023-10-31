package com.djavid.smartsubs.sort

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.common.SortNavigator
import com.djavid.smartsubs.common.models.SortBy
import com.djavid.smartsubs.common.models.SortType
import com.djavid.smartsubs.data.storage.SharedRepository
import com.djavid.smartsubs.utils.Constants.ACTION_REFRESH
import com.djavid.smartsubs.utils.Constants.KEY_SORT_BY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SortPresenter(
    private val view: SortContract.View,
    private val sharedRepository: SharedRepository,
    private val sortNavigator: SortNavigator,
    private val pipeline: BasePipeline<Pair<String, String>>,
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
                    KEY_SORT_BY -> {
                        loadSortBy()
                        pipeline.postValue(Pair(ACTION_REFRESH, ""))
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
            pipeline.postValue(Pair(KEY_SORT_BY, item.name))
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
                pipeline.postValue(Pair(ACTION_REFRESH, ""))
                logger.onSortTypeChanged(sortType)
            }
        }
    }

    override fun onDescSortClicked() {
        launch {
            if (sharedRepository.selectedSortType != SortType.DESC) {
                val sortType = SortType.DESC
                updateSortType(sortType)
                pipeline.postValue(Pair(ACTION_REFRESH, ""))
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