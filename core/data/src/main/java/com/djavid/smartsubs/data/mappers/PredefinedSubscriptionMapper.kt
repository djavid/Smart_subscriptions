package com.djavid.smartsubs.data.mappers

import com.djavid.smartsubs.common.entity.PredefinedSubscriptionFirebaseEntity
import com.djavid.smartsubs.common.domain.PredefinedSubscription
import com.djavid.smartsubs.data.storage.CloudStorageRepository

class PredefinedSubscriptionMapper(
    private val storageRepository: CloudStorageRepository
) {

    suspend fun toModel(entity: PredefinedSubscriptionFirebaseEntity): PredefinedSubscription? {
        val url = storageRepository.getSubLogoUrl(entity.logoUrl) ?: return null

        return PredefinedSubscription(
            subId = entity.id,
            title = entity.title,
            logoUrl = url,
            abbreviations = entity.abbreviations
        )
    }

}