package com.djavid.smartsubs.data.interactors

import com.djavid.smartsubs.common.models.Notification
interface AlarmInteractor {
    fun setAlarm(model: Notification)
    fun cancelAlarm(notifId: Long)
}