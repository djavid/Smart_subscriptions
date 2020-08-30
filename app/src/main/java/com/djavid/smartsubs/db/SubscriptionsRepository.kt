package com.djavid.smartsubs.db

import com.djavid.smartsubs.SubscriptionEntityQueries
import com.djavid.smartsubs.mappers.SubscriptionEntityMapper
import com.djavid.smartsubs.models.SubscriptionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate

class SubscriptionsRepository(
    private val queries: SubscriptionEntityQueries,
    private val entityMapper: SubscriptionEntityMapper,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    suspend fun updateTrialSubs(): List<SubscriptionDao> = withContext(Dispatchers.IO) {
        getSubs().forEach {
            if (it.trialPaymentDate != null && LocalDate.now().isAfter(it.trialPaymentDate)) {
                val sub = it.copy(paymentDate = it.trialPaymentDate, trialPaymentDate = null)
                queries.edit(entityMapper.toEntity(sub))
            }
        }
        getSubs()
    }

    suspend fun getSubs(): List<SubscriptionDao> = withContext(Dispatchers.IO) {
        queries.getSubscriptions().executeAsList().map {
            entityMapper.fromEntity(it)
        }
    }

    suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) { //todo use this to show suggestions
        queries.getCategories().executeAsList().mapNotNull { it.category }
    }

    suspend fun getSubById(id: Long): SubscriptionDao? = withContext(Dispatchers.IO) {
        val entity = queries.getSubscriptionById(id).executeAsOneOrNull()

        entity?.let {
            entityMapper.fromEntity(entity)
        }
    }

    suspend fun saveSub(model: SubscriptionDao) = withContext(Dispatchers.IO) {
        val entity = entityMapper.toEntity(model)
        queries.insert(entity)
    }

    suspend fun editSub(model: SubscriptionDao) = withContext(Dispatchers.IO) {
        val entity = entityMapper.toEntity(model)
        queries.edit(entity)
    }

    suspend fun deleteSubById(id: Long) = withContext(Dispatchers.IO) {
        queries.deleteSubById(id)
    }

    suspend fun deleteAllSubs() = withContext(Dispatchers.IO) {
        queries.deleteAllSubs()
    }

}