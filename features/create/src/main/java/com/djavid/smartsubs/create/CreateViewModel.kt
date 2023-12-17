package com.djavid.smartsubs.create

import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import kotlinx.coroutines.flow.Flow

interface CreateViewModel {
    val predefinedSubsFlow: Flow<List<PredefinedSuggestionItem>>
}