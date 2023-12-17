package com.djavid.smartsubs.data.interactors

import com.djavid.smartsubs.data.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.common.domain.SubscriptionUIModel
import com.djavid.smartsubs.data.storage.RealTimeRepository

class GetSubsInteractor(
    private val repository: RealTimeRepository,
    private val modelMapper: SubscriptionModelMapper
) {

    suspend fun execute(): List<SubscriptionUIModel> {
        return repository.getSubs().map { modelMapper.fromDao(it) }
    }

}