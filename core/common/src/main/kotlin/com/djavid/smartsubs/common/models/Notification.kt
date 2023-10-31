package com.djavid.smartsubs.common.models

import org.joda.time.DateTime

data class Notification(
    val id: Long,
    val subId: Long,
    val creationDate: DateTime,
    val isRepeating: Boolean,
    val daysBefore: Long, // if 0 than show in payment date
    val atDateTime: DateTime,
    val isActive: Boolean
)