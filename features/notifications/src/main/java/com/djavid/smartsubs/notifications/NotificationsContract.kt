package com.djavid.smartsubs.notifications

import com.djavid.smartsubs.common.models.Notification

interface NotificationsContract {

    interface View {
        fun init(presenter: Presenter)
        fun showNotifications(items: List<Notification>)
    }

    interface Presenter {
        fun init(subId: String?)
        fun onEditNotification(model: Notification)
        fun onNotifCheckChanged(notif: Notification, checked: Boolean)
        fun onAddNotification()
    }

}