package com.djavid.smartsubs.create

import androidx.lifecycle.ViewModel
import com.djavid.smartsubs.common.domain.PredefinedSubscription
import com.djavid.smartsubs.data.storage.PredefinedSubscriptionRepository
import kotlinx.coroutines.flow.Flow

class CreateViewModelImpl(
    repository: PredefinedSubscriptionRepository
) : ViewModel(), CreateViewModel {

    override val predefinedSubsFlow: Flow<List<PredefinedSubscription>> = repository.predefinedSubsWithLogoFlow
}