package com.djavid.smartsubs.notification

interface NotificationContract {

    interface View {
        fun init(presenter: Presenter)
    }

    interface Presenter {
        fun init(subscriptionId: Long, id: Long?)
    }

    interface Navigator {
        fun showNotificationDialog(subscriptionId: Long, id: Long? = null)
    }

}