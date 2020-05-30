package com.djavid.smartsubs.mappers

import android.app.PendingIntent
import com.djavid.smartsubs.PendingIntentEntity
import com.google.gson.Gson

class PendingIntentEntityMapper {

    fun toEntity(model: PendingIntent): PendingIntentEntity {
        val jsonString = Gson().toJson(model)
        return PendingIntentEntity.Impl(0, jsonString)
    }

    fun fromEntity(entity: PendingIntentEntity): PendingIntent {
        return Gson().fromJson(entity.jsonString, PendingIntent::class.java)
    }

}