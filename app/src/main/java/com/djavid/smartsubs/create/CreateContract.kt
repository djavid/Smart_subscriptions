package com.djavid.smartsubs.create

import com.djavid.smartsubs.models.SubscriptionPeriodType
import org.joda.time.DateTime
import java.util.*

interface CreateContract {

    interface Presenter {
        fun init()
        fun onCancelPressed()
        fun onBackPressed()

        //form
        fun onTitleInputChanged(input: String?)

        fun onPriceInputChanged(input: Double?)

        fun onPeriodQuantityInputChanged(input: Int?)
        fun onPeriodItemSelected(position: Int)

        fun onPaymentDateInputPressed()
        fun onPaymentDateInputChanged(input: DateTime)

        fun onCommentInputChanged(input: String?)

        fun onSubmitPressed()
    }

    interface View {
        fun init(presenter: Presenter)
        fun getPeriodString(period: SubscriptionPeriodType, quantity: Int): String
        fun setupSpinner(periods: List<String>)
        fun expandPanel()
        fun openDatePicker(prevSelectedDate: DateTime?)
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun selectPeriodItem(position: Int)
        fun hideKeyboard()
        fun setDateInput(text: String)
        fun setCurrencySymbol(currency: Currency)
        fun notifyToRefreshSubs()
        fun setEveryPlural(quantity: Int)
    }

    interface Navigator {
        fun goToCreateScreen()
    }

}