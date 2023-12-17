package com.djavid.smartsubs.sub_list

import com.djavid.smartsubs.common.domain.PredefinedSubscription
import kotlinx.coroutines.flow.Flow

interface SubListViewModel {
    val predefinedSubsFlow: Flow<List<PredefinedSubscription>>
}