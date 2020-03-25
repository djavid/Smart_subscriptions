package com.djavid.smartsubs.subscription

import android.view.View

class SubscriptionView(
    private val viewRoot: View
): SubscriptionContract.View {

    private lateinit var presenter: SubscriptionContract.Presenter

    override fun init(presenter: SubscriptionContract.Presenter) {
        this.presenter = presenter
    }

    override fun expandPanel() {

    }

}