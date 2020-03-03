package com.djavid.smartsubs.root

interface RootContract {

    interface Presenter {
        fun init()
        fun goToHomeScreen()
    }

    interface View {
        fun init(presenter: Presenter)
    }

}