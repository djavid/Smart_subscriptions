package com.djavid.smartsubs.db

import com.djavid.smartsubs.SubscriptionEntityQueries
import com.djavid.smartsubs.mappers.SubscriptionMapper
import com.djavid.smartsubs.models.Subscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubscriptionsRepository(
    private val queries: SubscriptionEntityQueries,
    private val mapper: SubscriptionMapper,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    suspend fun getSubs(): List<Subscription> = withContext(Dispatchers.IO) {
        queries.getSubscriptions().executeAsList().map {
            mapper.fromEntity(it)
        }
    }

    suspend fun getSubById(id: Long): Subscription? = withContext(Dispatchers.IO) {
        val entity = queries.getSubscriptionById(id).executeAsOneOrNull()

        entity?.let {
            mapper.fromEntity(entity)
        }
    }

    suspend fun saveSub(model: Subscription) = withContext(Dispatchers.IO) {
        val entity = mapper.toEntity(model)
        queries.insert(entity)
    }

}