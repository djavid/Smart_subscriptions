package com.djavid.smartsubs.sub_list

import androidx.lifecycle.ViewModel
import com.djavid.smartsubs.common.domain.PredefinedSubscription
import com.djavid.smartsubs.data.storage.PredefinedSubRepository
import kotlinx.coroutines.flow.Flow

class SubListViewModelImpl(
    repository: PredefinedSubRepository
) : SubListViewModel, ViewModel() {

    override val predefinedSubsFlow: Flow<List<PredefinedSubscription>> = repository.predefinedSubsWithLogoFlow

}