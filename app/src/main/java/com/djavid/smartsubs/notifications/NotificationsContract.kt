package com.djavid.smartsubs.notifications

import com.djavid.smartsubs.models.Notification

interface NotificationsContract {

    interface View {
        fun init(presenter: Presenter)
        fun showNotifications(items: List<Notification>)
    }

    interface Presenter {
        fun init(subId: Long)
        fun onEditNotification(model: Notification)
        fun onNotifCheckChanged(notif: Notification, checked: Boolean)
        fun onAddNotification()
    }

    interface Navigator {
        fun showNotificationsDialog(subId: Long)
    }

}