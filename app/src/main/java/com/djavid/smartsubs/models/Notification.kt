package com.djavid.smartsubs.models

import org.joda.time.LocalTime

data class Notification(
    val id: Long,
    val subId: Long,
    val isRepeating: Boolean,
    val daysBefore: Long, // if 0 than show in payment date
    val time: LocalTime,
    val isActive: Boolean
)