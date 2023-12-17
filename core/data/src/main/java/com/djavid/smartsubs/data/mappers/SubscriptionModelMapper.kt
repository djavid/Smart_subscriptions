package com.djavid.smartsubs.data.mappers

import com.djavid.smartsubs.common.models.Subscription
import com.djavid.smartsubs.common.models.SubscriptionPrice
import com.djavid.smartsubs.common.models.SubscriptionProgress
import com.djavid.smartsubs.common.models.SubscriptionDao
import com.djavid.smartsubs.data.storage.CloudStorageRepository
import com.djavid.smartsubs.data.storage.RealTimeRepository
import com.djavid.smartsubs.common.utils.addPeriod
import com.djavid.smartsubs.common.utils.getFirstPeriodBeforeNow
import com.djavid.smartsubs.common.utils.getPeriodsCountBeforeNow
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
        val paymentDate = dao.paymentDate

        return if (paymentDate != null) {
            val currentPeriodStart = paymentDate.getFirstPeriodBeforeNow(dao.period) //todo fix nullable
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
        val logoUrl = dao.predefinedSubId?.let { subId ->
            repository.getPredefinedSub(subId)?.logoUrl?.let { logoUrl ->
                storageRepository.getSubLogoUrl(logoUrl)
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
            logoUrl
        )
    }
}