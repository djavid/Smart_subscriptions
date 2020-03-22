package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionProgress
import com.djavid.smartsubs.utils.addPeriod
import com.djavid.smartsubs.utils.getFirstPeriodAfterNow
import com.djavid.smartsubs.utils.getFirstPeriodBeforeNow
import org.joda.time.DateTime
import org.joda.time.Days

class SubscriptionModelMapper {

    fun fromDao(dao: SubscriptionDao): Subscription {

        val subscriptionProgress = dao.paymentDate?.let { paymentDate ->
            val currentPeriodStart = paymentDate.getFirstPeriodBeforeNow(dao.period)
            val currentPeriodEnd = currentPeriodStart.addPeriod(dao.period)

            val daysLeft = Days.daysBetween(DateTime(), currentPeriodEnd).days
            val lastPeriodDaysAmount = Days.daysBetween(currentPeriodStart, currentPeriodEnd).days
            val progress = 1 - daysLeft / lastPeriodDaysAmount.toDouble()

            SubscriptionProgress(daysLeft, progress)
        }

        return Subscription(
            dao.id,
            dao.title,
            dao.price,
            dao.currency,
            dao.period,
            subscriptionProgress
        )
    }

//    fun toDao(model: Subscription): SubscriptionDao { todo
//
//    }

}