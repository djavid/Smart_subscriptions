package com.djavid.smartsubs.mappers

import com.djavid.smartsubs.NotificationEntity
import com.djavid.smartsubs.models.Notification
import org.joda.time.LocalTime

class NotificationEntityMapper {

    fun toEntity(model: Notification): NotificationEntity {
        return NotificationEntity.Impl(
            model.id,
            model.subId,
            model.repeating,
            model.daysBefore,
            model.time.millisOfDay.toLong(),
            model.active
        )
    }

    fun fromEntity(entity: NotificationEntity): Notification {
        return Notification(
            entity.id,
            entity.subId,
            entity.repeating,
            entity.daysBefore,
            LocalTime.fromMillisOfDay(entity.millisOfDay),
            entity.active
        )
    }

}