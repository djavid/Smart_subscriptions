package com.djavid.smartsubs.root

interface RootContract {

    interface Presenter {
        fun init(subId: String?)
    }

    interface View {
        fun init(presenter: Presenter)
    }

}