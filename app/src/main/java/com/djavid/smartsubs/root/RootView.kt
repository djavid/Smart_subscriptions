package com.djavid.smartsubs.root

import com.djavid.smartsubs.databinding.ActivityRootBinding

class RootView(
    val binding: ActivityRootBinding
) : RootContract.View {

    private lateinit var presenter: RootContract.Presenter

    override fun init(presenter: RootContract.Presenter) {
        this.presenter = presenter
    }

}