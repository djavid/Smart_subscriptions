package com.djavid.smartsubs.create

import com.djavid.smartsubs.create.CreateView.Companion.SLIDE_DURATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class CreatePresenter(
    val view: CreateContract.View,
    coroutineScope: CoroutineScope
) : CreateContract.Presenter, CoroutineScope by coroutineScope {

    override fun init() {
        view.init(this)

        view.expandPanel()
        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)
    }

    override fun onPanelExpanded() {
        //no-op
    }

    override fun onCancelPressed() {
        launch {
            view.collapsePanel()
            view.showToolbar(false, SLIDE_DURATION)
            view.setBackgroundTransparent(true, SLIDE_DURATION)
            delay(SLIDE_DURATION)
            view.goBack()
        }
    }

}