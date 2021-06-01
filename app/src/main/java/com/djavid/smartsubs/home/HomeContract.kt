package com.djavid.smartsubs.home

import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.SubscriptionPrice

interface HomeContract {

    interface Presenter {
        fun init()
        fun reloadSubs()
        fun onSortBtnPressed()
        fun onAddSubPressed()
        fun onPeriodPressed()
        fun onItemSwipedToLeft(position: Int)
        fun onItemClick(id: String)
    }

    interface View {
        fun init(presenter: Presenter)
        fun slidePanelToTop()
        fun showSubs(subs: List<Subscription>, pricePeriod: SubscriptionPeriodType)
        fun updateListPrices(pricePeriod: SubscriptionPeriodType)
        fun setSubsCount(count: Int)
        fun setSubsPrice(price: SubscriptionPrice)
        fun setSubsPeriod(period: SubscriptionPeriodType)
        fun showEmptyPlaceholder(show: Boolean)
        fun showProgress(show: Boolean)
    }

    interface Navigator {
        fun goToHome()
    }

}