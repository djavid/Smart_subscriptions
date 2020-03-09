package com.djavid.smartsubs.create

import android.view.View

class CreateView(
    private val viewRoot: View
) : CreateContract.View {

    private lateinit var presenter: CreateContract.Presenter

    override fun init(presenter: CreateContract.Presenter) {
        this.presenter = presenter
    }

}