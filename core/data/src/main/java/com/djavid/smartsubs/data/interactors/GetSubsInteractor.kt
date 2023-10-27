package com.djavid.smartsubs.data.interactors

import com.djavid.smartsubs.data.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.data.models.Subscription
import com.djavid.smartsubs.data.storage.RealTimeRepository

class GetSubsInteractor(
    private val repository: RealTimeRepository,
    private val modelMapper: SubscriptionModelMapper
) {

    suspend fun execute(): List<Subscription> {
        return repository.getSubs().map { modelMapper.fromDao(it) }
    }

}