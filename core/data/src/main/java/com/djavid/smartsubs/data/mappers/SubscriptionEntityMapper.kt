package com.djavid.smartsubs.data.mappers

import com.djavid.smartsubs.SubscriptionEntity
import com.djavid.smartsubs.common.models.SubscriptionDao
import com.djavid.smartsubs.common.models.SubscriptionFirebaseEntity
import com.djavid.smartsubs.common.models.SubscriptionPeriod
import com.djavid.smartsubs.common.models.SubscriptionPeriodType
import com.djavid.smartsubs.common.utils.atStartOfDayWithDefaultTimeZoneMillis
import com.djavid.smartsubs.common.utils.localNow
import com.djavid.smartsubs.common.utils.toLocalDate
import org.joda.time.DateTime
import java.util.*

class SubscriptionEntityMapper {

    fun toFirebaseEntity(model: SubscriptionDao): SubscriptionFirebaseEntity {
        return SubscriptionFirebaseEntity(
            id = model.id,
            creationDate = model.creationDate.millis,
            title = model.title,
            price = model.price,
            currencyCode = model.currency.currencyCode,
            periodQuantity = model.period.quantity.toLong(),
            period = model.period.type.name,
            category = model.category,
            comment = model.comment,
            paymentDate = model.paymentDate?.atStartOfDayWithDefaultTimeZoneMillis(),
            trialPaymentDate = if (model.hasTrialEnded()) null else model.trialPaymentDate?.atStartOfDayWithDefaultTimeZoneMillis(),
            loaded = true,
            predefinedSubId = model.predefinedSubId
        )
    }

    fun fromFirebaseEntity(entity: SubscriptionFirebaseEntity): SubscriptionDao {
        return SubscriptionDao(
            id = entity.id,
            creationDate = DateTime(entity.creationDate),
            title = entity.title,
            price = entity.price,
            currency = Currency.getInstance(entity.currencyCode),
            period = SubscriptionPeriod(
                type = SubscriptionPeriodType.valueOf(entity.period),
                quantity = entity.periodQuantity.toInt()
            ),
            category = entity.category,
            comment = entity.comment,
            paymentDate = entity.paymentDate?.toLocalDate(),
            trialPaymentDate = if (entity.hasTrialEnded()) null else entity.trialPaymentDate?.toLocalDate(),
            predefinedSubId = entity.predefinedSubId
        )
    }

    fun fromEntity(entity: SubscriptionEntity): SubscriptionDao {
        return SubscriptionDao(
            id = entity.id.toString(),
            creationDate = DateTime(entity.creationDate),
            title = entity.title,
            price = entity.price,
            currency = Currency.getInstance(entity.currencyCode),
            period = SubscriptionPeriod(
                SubscriptionPeriodType.valueOf(entity.period), entity.periodQuantity.toInt()
            ),
            category = entity.category,
            comment = entity.comment,
            paymentDate = entity.paymentDate?.toLocalDate(),
            trialPaymentDate = if (entity.hasTrialEnded()) null else entity.trialPaymentDate?.toLocalDate(),
            predefinedSubId = null
        )
    }


    private fun SubscriptionEntity.hasTrialEnded(): Boolean =
        trialPaymentDate != null && trialPaymentDate.let { localNow().isAfter(it.toLocalDate()) }
}