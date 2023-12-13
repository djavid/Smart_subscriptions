package com.djavid.smartsubs.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.base.BasePipeline
import com.djavid.smartsubs.common.models.SortBy
import com.djavid.smartsubs.common.models.SortType
import com.djavid.smartsubs.common.navigation.SortNavigator
import com.djavid.smartsubs.common.utils.Constants
import com.djavid.smartsubs.data.storage.SharedRepository
import kotlinx.coroutines.launch

class SortViewModelImpl(
    private val sharedRepository: SharedRepository,
    private val sortNavigator: SortNavigator,
    private val pipeline: BasePipeline<Pair<String, String>>,
    private val logger: FirebaseLogger
) : SortViewModel, ViewModel() {

    override val sortByValues: LiveData<List<SortBy>> = MutableLiveData(SortBy.entries)

    private val _selectedSortType: MutableLiveData<SortType> = MutableLiveData(sharedRepository.selectedSortType)
    override val selectedSortType: LiveData<SortType> = _selectedSortType

    private val _selectedSortBy = MutableLiveData(sharedRepository.selectedSortBy)
    override val selectedSortBy: LiveData<SortBy> = _selectedSortBy

    override fun selectSortByItem(item: SortBy) {
        viewModelScope.launch {
            sharedRepository.selectedSortBy = item
            _selectedSortBy.postValue(item)
            pipeline.postValue(Constants.ACTION_REFRESH to "")

            logger.onSortByChanged(item)
            sortNavigator.goBack()
        }
    }

    override fun selectSortType(sortType: SortType) {
        viewModelScope.launch {
            if (sharedRepository.selectedSortType != sortType) {
                sharedRepository.selectedSortType = sortType
                _selectedSortType.postValue(sortType)

                logger.onSortTypeChanged(sortType)
                pipeline.postValue(Constants.ACTION_REFRESH to "")
            }
        }
    }

    override fun openSortByScreen() {
        sortNavigator.openSortByScreen()
    }

}