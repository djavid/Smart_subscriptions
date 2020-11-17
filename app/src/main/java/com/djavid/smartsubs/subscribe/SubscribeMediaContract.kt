package com.djavid.smartsubs.subscribe

interface SubscribeMediaContract {

    interface Presenter {
        fun init()
        fun onNoClicked()
        fun onSubscribeClicked()
        fun tgAppOpenFailed()
        fun tgBrowserOpenFailed()
    }

    interface View {
        fun init(presenter: Presenter)
        fun close()
        fun openTgChannelInApp()
        fun openTgChannelInBrowser(channelUrl: String)
    }

    interface Navigator {
        fun showSubscribeDialog()
    }

}