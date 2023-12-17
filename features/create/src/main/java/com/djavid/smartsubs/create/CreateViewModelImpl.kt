package com.djavid.smartsubs.create

import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import com.djavid.smartsubs.data.storage.PredefinedSubRepository
import kotlinx.coroutines.flow.Flow

class CreateViewModelImpl(
    repository: PredefinedSubRepository
) : CreateViewModel {

    override val predefinedSubsFlow: Flow<List<PredefinedSuggestionItem>> = repository.predefinedSubsWithLogoFlow

}