package com.djavid.smartsubs.data.interactors

import com.djavid.smartsubs.common.domain.Notification
interface AlarmInteractor {
    fun setAlarm(model: Notification)
    fun cancelAlarm(notifId: Long)
}