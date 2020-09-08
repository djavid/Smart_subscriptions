package com.djavid.smartsubs.sort

import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.models.SortBy
import com.djavid.smartsubs.models.SortType
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.utils.FirebaseLogger
import com.djavid.smartsubs.utils.KEY_SORT_BY
import com.djavid.smartsubs.utils.SharedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class SortPresenter(
    private val view: SortContract.View,
    private val sharedRepository: SharedRepository,
    private val sortNavigator: SortContract.Navigator,
    private val pipeline: BasePipeline<Pair<String, String>>,
    private val logger: FirebaseLogger,
    coroutineScope: CoroutineScope
) : SortContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var channel: ReceiveChannel<Pair<String, String>>

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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun listenPipeline() {
        launch {
            channel = pipeline.subscribe()
            channel.consumeEach {
                when (it.first) {
                    KEY_SORT_BY -> {
                        loadSortBy()
                        pipeline.postValue(Pair(ACTION_REFRESH, ""))
                    }
                }
            }
        }
    }

    private fun showSortScreen() {
        view.showSortTitle(true)
        view.showSortDelimiter(true)

        loadSortBy()
        loadSortType()
    }

    private fun showSortByScreen() {
        view.showSortByList(SortBy.values().toList())
    }

    override fun onSortItemClicked(item: SortBy) {
        sharedRepository.selectedSortBy = item
        pipeline.postValue(Pair(KEY_SORT_BY, item.name))
        logger.onSortByChanged(item)
        view.finish()
    }

    override fun onSortByClicked() {
        sortNavigator.openSortByScreen()
    }

    override fun onAscSortClicked() {
        if (sharedRepository.selectedSortType != SortType.ASC) {
            val sortType = SortType.ASC
            updateSortType(sortType)
            pipeline.postValue(Pair(ACTION_REFRESH, ""))
            logger.onSortTypeChanged(sortType)
        }
    }

    override fun onDescSortClicked() {
        if (sharedRepository.selectedSortType != SortType.DESC) {
            val sortType = SortType.DESC
            updateSortType(sortType)
            pipeline.postValue(Pair(ACTION_REFRESH, ""))
            logger.onSortTypeChanged(sortType)
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