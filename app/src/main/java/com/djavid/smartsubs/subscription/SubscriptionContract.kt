package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.models.Notification
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
        fun showNotifications(items: List<Notification>)
    }

    interface Presenter {
        fun init(id: Long, isRoot: Boolean)
        fun reload()
        fun onBackPressed()
        fun onCloseBtnClicked()
        fun onEditClicked()
        fun onDeleteClicked()
        fun onAddNotification()
        fun onEditNotification(model: Notification)
        fun onNotifCheckChanged(notif: Notification, checked: Boolean)
    }

    interface Navigator {
        fun goToSubscription(id: Long, isRoot: Boolean = false) //isRoot means that homeFragment doesn't exist in backstack
    }

}