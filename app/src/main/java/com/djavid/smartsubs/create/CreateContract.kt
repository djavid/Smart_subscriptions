package com.djavid.smartsubs.create

interface CreateContract {

    interface Presenter {
        fun init()
        fun onCancelPressed()
        fun onPanelExpanded()
    }

    interface View {
        fun init(presenter: Presenter)
        fun expandPanel()
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun goBack()
    }

    interface Navigator {
        fun goToCreateScreen()
    }

}