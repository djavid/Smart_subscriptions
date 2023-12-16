package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.common.models.SubscriptionPeriod
import com.djavid.smartsubs.common.models.SubscriptionPrice
import com.djavid.smartsubs.common.models.SubscriptionProgress

interface SubscriptionContract {

    interface View {
        fun init(presenter: Presenter)
        fun setSubLogo(logoUrl: String?)
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
        fun reload(allowCache: Boolean)
        fun onBackPressed()
        fun onCloseBtnClicked()
        fun onEditClicked()
        fun onDeleteClicked()
        fun onDeletionPrompted()
        fun onNotifsClicked()
    }

}