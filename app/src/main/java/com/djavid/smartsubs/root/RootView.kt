package com.djavid.smartsubs.root

import android.view.View

class RootView(
    val viewRoot: View
) : RootContract.View {

    private lateinit var presenter: RootContract.Presenter

    override fun init(presenter: RootContract.Presenter) {
        this.presenter = presenter
    }

}