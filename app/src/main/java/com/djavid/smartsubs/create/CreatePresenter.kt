package com.djavid.smartsubs.create

import com.djavid.smartsubs.create.CreateView.Companion.SLIDE_DURATION
import kotlinx.coroutines.*

class CreatePresenter(
    val view: CreateContract.View,
    coroutineScope: CoroutineScope
) : CreateContract.Presenter, CoroutineScope by coroutineScope {

    override fun init() {
        view.init(this)

        view.expandPanel()
        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)

        launch {
            withContext(Dispatchers.Default) { delay(SLIDE_DURATION) }
            view.showSubmitBtn(true)
        }
    }

    override fun onPanelExpanded() {
        //no-op
    }

    override fun onCancelPressed() {
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun finish() {
        launch {
            view.collapsePanel()
            view.showSubmitBtn(false)
            view.showToolbar(false, SLIDE_DURATION)
            view.setBackgroundTransparent(true, SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(SLIDE_DURATION) }
            view.goBack()
        }
    }

}