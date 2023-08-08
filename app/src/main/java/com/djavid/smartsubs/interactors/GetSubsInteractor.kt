package com.djavid.smartsubs.interactors

import com.djavid.smartsubs.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.storage.RealTimeRepository

class GetSubsInteractor(
    private val repository: RealTimeRepository,
    private val modelMapper: SubscriptionModelMapper
) {

    suspend fun execute(): List<Subscription> {
        return repository.getSubs().map { modelMapper.fromDao(it) }
    }

}