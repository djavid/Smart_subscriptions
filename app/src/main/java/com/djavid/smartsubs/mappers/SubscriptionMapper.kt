package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.SubscriptionEntity
import com.djavid.smartsubs.models.Subscription

class SubscriptionMapper {

    fun toEntity(model: Subscription): SubscriptionEntity {
        return SubscriptionEntity.Impl(
            model.id,
            model.title,
            model.priceRubles,
            model.period,
            model.periodStart,
            model.periodEnd,
            model.comment
        )
    }

    fun fromEntity(entity: SubscriptionEntity): Subscription {
        return Subscription(
            entity.id,
            entity.title,
            entity.priceRubles,
            entity.period,
            entity.periodStart,
            entity.periodEnd,
            entity.comment
        )
    }

}