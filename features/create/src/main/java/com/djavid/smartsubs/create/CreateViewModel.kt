package com.djavid.smartsubs.create

import com.djavid.smartsubs.common.domain.PredefinedSubscription
import kotlinx.coroutines.flow.Flow

interface CreateViewModel {
    val predefinedSubsFlow: Flow<List<PredefinedSubscription>>
}