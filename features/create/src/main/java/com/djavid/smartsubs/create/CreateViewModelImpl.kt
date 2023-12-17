package com.djavid.smartsubs.create

import com.djavid.smartsubs.common.domain.PredefinedSubscription
import com.djavid.smartsubs.data.storage.PredefinedSubRepository
import kotlinx.coroutines.flow.Flow

class CreateViewModelImpl(
    repository: PredefinedSubRepository
) : CreateViewModel {

    override val predefinedSubsFlow: Flow<List<PredefinedSubscription>> = repository.predefinedSubsWithLogoFlow

}