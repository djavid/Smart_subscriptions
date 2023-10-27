package com.djavid.smartsubs.data.mappers

import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPrice
import com.djavid.smartsubs.models.SubscriptionProgress
import com.djavid.smartsubs.data.storage.CloudStorageRepository
import com.djavid.smartsubs.data.storage.RealTimeRepository
import com.djavid.common.addPeriod
import com.djavid.common.getFirstPeriodBeforeNow
import com.djavid.common.getPeriodsCountBeforeNow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.LocalDate
import java.util.*

class SubscriptionModelMapper(
    private val storageRepository: CloudStorageRepository,
    private val repository: RealTimeRepository
) {

    private fun getProgressForTrialSub(dao: SubscriptionDao): SubscriptionProgress? {
        return if (dao.trialPaymentDate != null) {
            val dateNow = LocalDate.now()

            val daysLeft = Days.daysBetween(dateNow, dao.trialPaymentDate).days
            val trialDaysAmount = Days.daysBetween(dao.creationDate.toLocalDate(), dao.trialPaymentDate).days

            val progress = if (trialDaysAmount != 0) {
                1 - daysLeft / trialDaysAmount.toDouble()
            } else {
                1.0
            }

            return SubscriptionProgress(daysLeft, progress)
        } else {
            null
        }
    }

    private fun getProgressForSub(dao: SubscriptionDao): SubscriptionProgress? {
        return if (dao.paymentDate != null) {
            val currentPeriodStart = dao.paymentDate.getFirstPeriodBeforeNow(dao.period)
            val currentPeriodEnd = currentPeriodStart.addPeriod(dao.period)
            val dateNow = LocalDate.now(DateTimeZone.forTimeZone(TimeZone.getDefault()))

            if (currentPeriodStart.isEqual(dateNow)) { //period start is today
                SubscriptionProgress(0, 1.0)
            } else {
                val daysLeft = Days.daysBetween(DateTime().toLocalDate(), currentPeriodEnd).days
                val lastPeriodDaysAmount = Days.daysBetween(currentPeriodStart, currentPeriodEnd).days
                val progress = 1 - daysLeft / lastPeriodDaysAmount.toDouble()

                SubscriptionProgress(daysLeft, progress)
            }
        } else {
            null
        }
    }

    suspend fun fromDao(dao: SubscriptionDao): Subscription = withContext(Dispatchers.IO) {
        val subscriptionProgress = when {
            dao.trialPaymentDate != null -> getProgressForTrialSub(dao)
            dao.paymentDate != null -> getProgressForSub(dao)
            else -> null
        }

        val overallSpent = dao.paymentDate?.let { paymentDate ->
            val periodsCount = paymentDate.getPeriodsCountBeforeNow(dao.period)

            if (periodsCount > 0) {
                periodsCount * dao.price
            } else {
                0.0
            }
        }

        val subscriptionPrice = SubscriptionPrice(dao.price, dao.currency)
        val logoBytes = dao.predefinedSubId?.let {
            repository.getPredefinedSub(it)?.logoUrl?.let { logoUrl ->
                storageRepository.getSubLogoBytes(logoUrl)
            }
        }

        Subscription(
            dao.id,
            dao.title,
            subscriptionPrice,
            dao.period,
            subscriptionProgress,
            dao.category,
            overallSpent,
            dao.comment,
            dao.trialPaymentDate,
            logoBytes
        )
    }

}