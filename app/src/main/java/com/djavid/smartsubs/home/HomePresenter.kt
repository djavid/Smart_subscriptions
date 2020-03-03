package com.djavid.smartsubs.home

class HomePresenter(
    private val view: HomeContract.View
) : HomeContract.Presenter {

    override fun init() {
        view.init(this)


    }

}