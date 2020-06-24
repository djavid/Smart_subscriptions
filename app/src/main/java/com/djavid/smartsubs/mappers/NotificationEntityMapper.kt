package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.NotificationEntity
import com.djavid.smartsubs.models.Notification
import org.joda.time.DateTime

class NotificationEntityMapper {

    fun toEntity(model: Notification): NotificationEntity {
        return NotificationEntity.Impl(
            model.id,
            model.subId,
            model.isRepeating,
            model.daysBefore,
            model.atDateTime.millis,
            model.isActive,
            model.subTitle
        )
    }

    fun fromEntity(entity: NotificationEntity): Notification {
        return Notification(
            entity.id,
            entity.subId,
            entity.repeating,
            entity.daysBefore,
            DateTime(entity.atMillis),
            entity.active,
            entity.subTitle
        )
    }

}