package com.djavid.smartsubs.sub_list

import com.djavid.smartsubs.models.PredefinedSuggestionItem

class SubListContract {
    interface View {
        fun init(presenter: Presenter)
        fun hideKeyboard()
        fun expandPanel()
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun showPredefinedSubs(list: List<PredefinedSuggestionItem>)
        fun showProgress(show: Boolean)
    }

    interface Presenter {
        fun init()
        fun onBackPressed()
        fun onItemClick(item: PredefinedSuggestionItem)
    }

    interface Navigator {
        fun goToSubListScreen()
    }

    companion object {
        const val REQUEST_KEY = "sublist_request_key"
        const val FRAGMENT_RESULT_KEY = "sublist_fragment_result_key"
    }

}