package com.djavid.smartsubs.subscription

interface SubscriptionContract {

    interface View {
        fun init(presenter: Presenter)
        fun expandPanel()
    }

    interface Presenter {
        fun init(id: Long)
        fun onBackPressed()
    }

    interface Navigator {
        fun goToSubscription(id: Long)
    }

}