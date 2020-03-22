package com.djavid.smartsubs.create

import com.djavid.smartsubs.models.SubscriptionPeriodType

interface CreateContract {

    interface Presenter {
        fun init()
        fun onCancelPressed()
        fun onBackPressed()
        fun onItemSelected(position: Int)
    }

    interface View {
        fun init(presenter: Presenter)
        fun getPeriodString(period: SubscriptionPeriodType): String
        fun setupSpinner(periods: List<String>)
        fun expandPanel()
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun goBack()
        fun selectPeriodItem(position: Int)
        fun hideKeyboard()
    }

    interface Navigator {
        fun goToCreateScreen()
    }

}