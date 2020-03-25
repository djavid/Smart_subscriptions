package com.djavid.smartsubs.subscription

interface SubscriptionContract {

    interface View {
        fun init(presenter: Presenter)
        fun expandPanel()
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun hideKeyboard()
        fun notifyToRefreshSubs()
    }

    interface Presenter {
        fun init(id: Long)
        fun onBackPressed()
        fun onCloseBtnClicked()
    }

    interface Navigator {
        fun goToSubscription(id: Long)
    }

}