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
        fun setTitle(title: String)
        fun setCategory(category: String)
        fun setPrice(period: SubscriptionPeriod, price: SubscriptionPrice)
        fun setComment(comment: String)
        fun setNextPayment(progress: SubscriptionProgress)
        fun setOverallSpent(spent: SubscriptionPrice)
        fun showDeletionPromptDialog()
        fun setNotifsCount(notifs: Int)
        fun showNotifsSection(show: Boolean)
    }

    interface Presenter {
        fun init(id: String?, isRoot: Boolean)
        fun reload()
        fun onBackPressed()
        fun onCloseBtnClicked()
        fun onEditClicked()
        fun onDeleteClicked()
        fun onDeletionPrompted()
        fun onNotifsClicked()
    }

    interface Navigator {
        fun goToSubscription(id: String, isRoot: Boolean = false) //isRoot means that homeFragment doesn't exist in backstack
    }

}