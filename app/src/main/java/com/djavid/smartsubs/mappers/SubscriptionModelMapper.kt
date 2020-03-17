package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionProgress
import com.djavid.smartsubs.utils.addPeriod
import org.joda.time.DateTime
import org.joda.time.Days

class SubscriptionModelMapper {

    fun fromDao(dao: SubscriptionDao): Subscription {

        val subscriptionProgress = dao.periodStart?.let { periodStart ->
            var nextPeriod = periodStart.addPeriod(dao.period)

            while (nextPeriod.isBeforeNow) {
                nextPeriod = nextPeriod.addPeriod(dao.period)
            }

            val daysLeft = Days.daysBetween(DateTime(), nextPeriod).days
            val lastPeriodDaysAmount = Days.daysBetween(periodStart, nextPeriod).days
            val progress = daysLeft / lastPeriodDaysAmount.toDouble()

            SubscriptionProgress(daysLeft, progress)
        }

        return Subscription(
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