package com.djavid.smartsubs.data.db

import com.djavid.smartsubs.NotificationEntityQueries
import com.djavid.smartsubs.data.mappers.NotificationEntityMapper
import com.djavid.smartsubs.common.domain.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationsRepository(
    private val queries: NotificationEntityQueries,
    private val entityMapper: NotificationEntityMapper,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    suspend fun getNotifications(): List<Notification> = withContext(Dispatchers.IO) {
        queries.getNotifications().executeAsList().map {
            entityMapper.fromEntity(it)
        }
    }

    suspend fun getNotificationById(id: Long): Notification? = withContext(Dispatchers.IO) {
        val entity = queries.getNotificationById(id).executeAsOneOrNull()

        return@withContext entity?.let {
            entityMapper.fromEntity(it)
        }
    }

    suspend fun getNotificationsBySubId(subId: String): List<Notification> = withContext(Dispatchers.IO) {
        queries.getNotificationsBySubscriptionId(0) //todo broken
            .executeAsList()
            .map { entityMapper.fromEntity(it) }
    }

    suspend fun saveNotification(model: Notification) = withContext(Dispatchers.IO) {
        val entity = entityMapper.toEntity(model)
        queries.insert(entity)
    }

    suspend fun editNotification(model: Notification) = withContext(Dispatchers.IO) {
        val entity = entityMapper.toEntity(model)
        queries.edit(entity)
    }

    suspend fun deleteNotificationsBySubId(subId: Long) = withContext(Dispatchers.IO) {
        queries.deleteNotificationsBySubscriptionId(subId)
    }

    suspend fun deleteNotificationById(id: Long) = withContext(Dispatchers.IO) {
        queries.deleteNotificationById(id)
    }

    suspend fun deleteAllNotifications() = withContext(Dispatchers.IO) {
        queries.deleteAllNotifications()
    }

}