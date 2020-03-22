package com.djavid.smartsubs.home

import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType
import java.util.*

interface HomeContract {

    interface Presenter {
        fun init()
        fun reloadSubs()
        fun onAddSubPressed()
        fun onPeriodPressed()
        fun onItemSwipedToLeft(position: Int)
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