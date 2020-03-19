package com.djavid.smartsubs.home

import com.djavid.smartsubs.models.Currency
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType

interface HomeContract {

    interface Presenter {
        fun init()
        fun onAddSubPressed()
        fun onPeriodPressed()
    }

    interface View {
        fun init(presenter: Presenter)
        fun slidePanelToTop()
        fun showSubs(subs: List<Subscription>)
        fun setSubsCount(count: Int)
        fun setSubsPrice(price: Double, currency: Currency)
        fun setSubsPeriod(period: SubscriptionPeriodType)
    }

    interface Navigator {
        fun goToHomeScreen()
    }

}