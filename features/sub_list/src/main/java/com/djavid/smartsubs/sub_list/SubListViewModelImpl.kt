package com.djavid.smartsubs.sub_list

import androidx.lifecycle.ViewModel
import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import com.djavid.smartsubs.data.storage.RealTimeRepository
import kotlinx.coroutines.flow.Flow

class SubListViewModelImpl(
    repository: RealTimeRepository
) : SubListViewModel, ViewModel() {

    override val predefinedSubsFlow: Flow<List<PredefinedSuggestionItem>> = repository.predefinedSubsWithLogoFlow

}