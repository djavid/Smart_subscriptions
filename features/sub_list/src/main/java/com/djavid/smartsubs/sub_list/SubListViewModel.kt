package com.djavid.smartsubs.sub_list

import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import kotlinx.coroutines.flow.Flow

interface SubListViewModel {
    val predefinedSubsFlow: Flow<List<PredefinedSuggestionItem>>
}