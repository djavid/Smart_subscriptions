package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.SubscriptionEntity
import com.djavid.smartsubs.models.Currency
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import org.joda.time.DateTime

class SubscriptionEntityMapper {

    fun toEntity(model: SubscriptionDao): SubscriptionEntity {
        return SubscriptionEntity.Impl(
            model.id,
            model.title,
            model.price,
            model.currency.name,
            model.period.quantity.toLong(),
            model.period.type.name,
            model.periodStart?.millis,
            model.comment
        )
    }

    fun fromEntity(entity: SubscriptionEntity): SubscriptionDao {
        return SubscriptionDao(
            entity.id,
            entity.title,
            entity.price,
            Currency.valueOf(entity.currency),
            SubscriptionPeriod(
                SubscriptionPeriodType.valueOf(entity.period), entity.periodQuantity.toInt()
            ),
            entity.periodStart?.let { DateTime(it) },
            entity.comment
        )
    }

}