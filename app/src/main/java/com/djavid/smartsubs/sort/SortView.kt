package com.djavid.smartsubs.sort

import android.view.View

class SortView(
    private val viewRoot: View
) : SortContract.View {

    private lateinit var presenter: SortContract.Presenter

    override fun init(presenter: SortContract.Presenter) {
        this.presenter = presenter
    }

}