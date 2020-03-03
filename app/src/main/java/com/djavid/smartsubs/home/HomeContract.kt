package com.djavid.smartsubs.home

interface HomeContract {

    interface Presenter {
        fun init()
    }

    interface View {
        fun init(presenter: Presenter)
    }

    interface Navigator {
        fun goToHome()
    }

}