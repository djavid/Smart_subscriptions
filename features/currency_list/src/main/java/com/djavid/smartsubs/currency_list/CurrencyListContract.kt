package com.djavid.smartsubs.currency_list

import com.djavid.smartsubs.common.domain.PredefinedSubscription

class CurrencyListContract {
    interface View {
        fun init(presenter: Presenter)
        fun hideKeyboard()
        fun expandPanel()
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun showPredefinedSubs(list: List<PredefinedSubscription>)
        fun showProgress(show: Boolean)
    }

    interface Presenter {
        fun init()
        fun onBackPressed()
        fun onItemClick(item: PredefinedSubscription)
    }

    companion object {
        const val REQUEST_KEY = "sublist_request_key"
        const val FRAGMENT_RESULT_KEY = "sublist_fragment_result_key"
    }

}