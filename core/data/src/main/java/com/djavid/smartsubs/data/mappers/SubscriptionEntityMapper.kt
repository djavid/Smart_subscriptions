package com.djavid.smartsubs.data.mappers

import com.djavid.smartsubs.SubscriptionEntity
import com.djavid.smartsubs.common.models.SubscriptionDao
import com.djavid.smartsubs.common.models.SubscriptionFirebaseEntity
import com.djavid.smartsubs.common.models.SubscriptionPeriod
import com.djavid.smartsubs.common.models.SubscriptionPeriodType
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.*

class SubscriptionEntityMapper {

    fun fromEntity(entity: SubscriptionEntity): SubscriptionDao {
        return SubscriptionDao(
            entity.id.toString(),
            DateTime(entity.creationDate),
            entity.title,
            entity.price,
            Currency.getInstance(entity.currencyCode),
            SubscriptionPeriod(
                SubscriptionPeriodType.valueOf(entity.period), entity.periodQuantity.toInt()
            ),
            entity.paymentDate?.let { LocalDate(it) },
            entity.category,
            entity.comment,
            entity.trialPaymentDate?.let { LocalDate(it) },
            null
        )
    }

    fun toFirebaseEntity(model: SubscriptionDao): SubscriptionFirebaseEntity {
        val timeZone = DateTimeZone.forTimeZone(TimeZone.getDefault())

        return SubscriptionFirebaseEntity(
            model.id,
            model.creationDate.millis,
            model.title,
            model.price,
            model.currency.currencyCode,
            model.period.quantity.toLong(),
            model.period.type.name,
            model.paymentDate?.toDateTimeAtStartOfDay(timeZone)?.millis,
            model.category,
            model.comment,
            model.trialPaymentDate?.toDateTimeAtStartOfDay(timeZone)?.millis,
            true,
            model.predefinedSubId
        )
    }

    fun fromFirebaseEntity(entity: SubscriptionFirebaseEntity): SubscriptionDao {
        return SubscriptionDao(
            entity.id,
            DateTime(entity.creationDate),
            entity.title,
            entity.price,
            Currency.getInstance(entity.currencyCode),
            SubscriptionPeriod(
                SubscriptionPeriodType.valueOf(entity.period), entity.periodQuantity.toInt()
            ),
            entity.paymentDate?.let { LocalDate(it) },
            entity.category,
            entity.comment,
            entity.trialPaymentDate?.let { LocalDate(it) },
            entity.predefinedSubId
        )
    }

}