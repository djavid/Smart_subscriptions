package com.djavid.smartsubs.sort

interface SortContract {

    interface Presenter {
        fun init()
    }

    interface View {
        fun init(presenter: Presenter)
    }

    interface Navigator {
        fun openSortScreen()
    }

}