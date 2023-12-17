package com.djavid.smartsubs.sub_list

import com.djavid.smartsubs.common.models.PredefinedSuggestionItem

class SubListContract {
    interface View {
        fun init(presenter: Presenter)
        fun destroy()
        fun hideKeyboard()
        fun expandPanel()
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun showProgress(show: Boolean)
    }

    interface Presenter {
        fun onBackPressed()
        fun onItemClick(item: PredefinedSuggestionItem)
    }

    companion object {
        const val REQUEST_KEY = "sublist_request_key"
        const val FRAGMENT_RESULT_KEY = "sublist_fragment_result_key"
    }

}