package com.djavid.smartsubs.subscribe

interface SubscribeMediaContract {

    interface Presenter {
        fun init()
        fun onNoClicked()
        fun onSubscribeClicked()
    }

    interface View {
        fun init(presenter: Presenter)
        fun close()
        fun openTgChannel()
    }

    interface Navigator {
        fun showSubscribeDialog()
    }

}