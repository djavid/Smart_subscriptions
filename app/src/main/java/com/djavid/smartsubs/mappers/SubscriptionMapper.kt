package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.SubscriptionEntity
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.utils.Instant

class SubscriptionMapper {

    fun toEntity(model: SubscriptionDao): SubscriptionEntity {
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

    fun fromEntity(entity: SubscriptionEntity): SubscriptionDao {
        return SubscriptionDao(
            entity.id,
            entity.title,
            entity.priceRubles,
            entity.period,
            entity.periodStart,
            entity.periodEnd,
            entity.comment
        )
    }

    fun fromDao(dao: SubscriptionDao): Subscription { //todo
        return Subscription(
            "Apple Music",
            69.0,
            SubscriptionPeriod(SubscriptionPeriodType.MONTH),
            Instant(System.currentTimeMillis()),
            15,
            50
        )
    }

//    fun toDao(model: Subscription): SubscriptionDao { todo
//
//    }

}