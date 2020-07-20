package com.djavid.smartsubs.sort

class SortPresenter(
    private val view: SortContract.View
) : SortContract.Presenter {

    override fun init() {
        view.init(this)
    }

}