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

    suspend fun getSubs(): List<SubscriptionDao> = withContext(Dispatchers.IO) {
        queries.getSubscriptions().executeAsList().map {
            entityMapper.fromEntity(it)
        }
    }

    suspend fun deleteAllSubs() = withContext(Dispatchers.IO) {
        queries.deleteAllSubs()
    }

}