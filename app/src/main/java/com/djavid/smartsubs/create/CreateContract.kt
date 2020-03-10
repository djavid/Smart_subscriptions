package com.djavid.smartsubs.create

interface CreateContract {

    interface Presenter {
        fun init()
        fun onCancelPressed()
    }

    interface View {
        fun init(presenter: Presenter)
    }

    interface Navigator {
        fun goToCreateScreen(addBtn: android.view.View)
    }

}