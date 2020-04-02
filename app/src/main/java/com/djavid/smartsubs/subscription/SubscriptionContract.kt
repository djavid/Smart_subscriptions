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

//    data class NotifFormModel(
//        val frequency: NotifFrequency,
//        val daysTypeChecked: Boolean,
//        val dayTypeChecked: Boolean,
//        val daysBefore: Int,
//        val daysTime: LocalTime,
//        val dayTime: LocalTime
//    )
//
//    data class Notif(
//        val notifId: Long,
//        val notifDateTime: DateTime
//        //todo thing about repeat
//    )
//
//    enum class NotifFrequency {
//        ONE_TIME, EVERY_TIME
//    }

}