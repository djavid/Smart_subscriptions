package com.djavid.smartsubs.home

import android.view.View

class HomeView(
    val viewRoot: View
) : HomeContract.View {

    private lateinit var presenter: HomeContract.Presenter

    override fun init(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

}