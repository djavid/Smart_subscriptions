package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPrice
import com.djavid.smartsubs.models.SubscriptionProgress

interface SubscriptionContract {

    interface View {
        fun init(presenter: Presenter)
        fun expandPanel(biggerToolbar: Boolean)
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun hideKeyboard()
        fun notifyToRefresh()
        fun setTitle(title: String)
        fun setCategory(category: String)
        fun setPrice(period: SubscriptionPeriod, price: SubscriptionPrice)
        fun setComment(comment: String)
        fun setNextPayment(progress: SubscriptionProgress)
        fun setOverallSpent(spent: SubscriptionPrice)
    }

    interface Presenter {
        fun init(id: Long)
        fun reload()
        fun onBackPressed()
        fun onCloseBtnClicked()
        fun onEditClicked()
        fun onDeleteClicked()
    }

    interface Navigator {
        fun goToSubscription(id: Long)
    }

}