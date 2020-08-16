package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.SubscriptionEntity
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.*

class SubscriptionEntityMapper {

    fun toEntity(model: SubscriptionDao): SubscriptionEntity {
        val timeZone = DateTimeZone.forTimeZone(TimeZone.getDefault())

        return SubscriptionEntity.Impl(
            model.id,
            model.title,
            model.price,
            model.currency.currencyCode,
            model.period.quantity.toLong(),
            model.period.type.name,
            model.paymentDate?.toDateTimeAtStartOfDay(timeZone)?.millis,
            model.category,
            model.comment,
            model.trialPaymentDate?.toDateTimeAtStartOfDay(timeZone)?.millis
        )
    }

    fun fromEntity(entity: SubscriptionEntity): SubscriptionDao {
        return SubscriptionDao(
            entity.id,
            entity.title,
            entity.price,
            Currency.getInstance(entity.currencyCode),
            SubscriptionPeriod(
                SubscriptionPeriodType.valueOf(entity.period), entity.periodQuantity.toInt()
            ),
            entity.paymentDate?.let { LocalDate(it) },
            entity.category,
            entity.comment,
            entity.trialPaymentDate?.let { LocalDate(it) }
        )
    }

}