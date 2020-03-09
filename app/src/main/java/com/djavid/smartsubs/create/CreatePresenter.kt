package com.djavid.smartsubs.create

class CreatePresenter(
    val view: CreateContract.View
) : CreateContract.Presenter {

    override fun init() {
        view.init(this)
    }

}