package com.djavid.smartsubs.notification

import android.view.View

class NotificationView(
    private val viewRoot: View
) : NotificationContract.View {

    private lateinit var presenter: NotificationContract.Presenter

    override fun init(presenter: NotificationContract.Presenter) {
        this.presenter = presenter
    }

}