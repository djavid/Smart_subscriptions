package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.NotificationEntity
import com.djavid.smartsubs.models.Notification
import org.joda.time.DateTime

class NotificationEntityMapper {

    fun toEntity(model: Notification): NotificationEntity {
        return NotificationEntity(
            model.id,
            model.subId,
            model.creationDate.millis,
            model.isRepeating,
            model.daysBefore,
            model.atDateTime.millis,
            model.isActive
        )
    }

    fun fromEntity(entity: NotificationEntity): Notification {
        return Notification(
            entity.id,
            entity.subId,
            DateTime(entity.creationDate),
            entity.repeating,
            entity.daysBefore,
            DateTime(entity.atMillis),
            entity.active
        )
    }

}