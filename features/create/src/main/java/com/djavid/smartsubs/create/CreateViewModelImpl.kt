package com.djavid.smartsubs.create

import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import com.djavid.smartsubs.data.storage.RealTimeRepository
import kotlinx.coroutines.flow.Flow

class CreateViewModelImpl(
    repository: RealTimeRepository
) : CreateViewModel {

    override val predefinedSubsFlow: Flow<List<PredefinedSuggestionItem>> = repository.predefinedSubsWithLogoFlow

}