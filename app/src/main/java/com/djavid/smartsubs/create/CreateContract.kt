package com.djavid.smartsubs.create

interface CreateContract {

    interface Presenter {
        fun init()
    }

    interface View {
        fun init(presenter: Presenter)
    }

    interface Navigator {
        fun goToCreateScreen(addBtn: android.view.View)
    }

}