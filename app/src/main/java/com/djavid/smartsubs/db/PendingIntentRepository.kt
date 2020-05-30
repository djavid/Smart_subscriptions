package com.djavid.smartsubs.db

import android.app.PendingIntent
import com.djavid.smartsubs.PendingIntentEntityQueries
import com.djavid.smartsubs.mappers.PendingIntentEntityMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PendingIntentRepository(
    private val queries: PendingIntentEntityQueries,
    private val entityMapper: PendingIntentEntityMapper,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    suspend fun getIntents(): List<PendingIntent> = withContext(Dispatchers.IO) {
        queries.getPendingIntents().executeAsList().map {
            entityMapper.fromEntity(it)
        }
    }

    suspend fun getIntentById(id: Long): PendingIntent? = withContext(Dispatchers.IO) {
        val entity = queries.getPendingIntentById(id).executeAsOneOrNull()

        entity?.let {
            entityMapper.fromEntity(entity)
        }
    }

    suspend fun saveIntent(intent: PendingIntent) = withContext(Dispatchers.IO) {
        val entity = entityMapper.toEntity(intent)
        queries.insert(entity)
    }

    suspend fun editIntent(model: PendingIntent) = withContext(Dispatchers.IO) {
        val entity = entityMapper.toEntity(model)
        queries.edit(entity)
    }

    suspend fun deleteIntentById(id: Long) = withContext(Dispatchers.IO) {
        queries.deletePendingIntentById(id)
    }

    suspend fun deleteAllIntents() = withContext(Dispatchers.IO) {
        queries.deleteAllPendingIntents()
    }

}