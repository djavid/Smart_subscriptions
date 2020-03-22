package com.djavid.smartsubs.create

import com.djavid.smartsubs.create.CreateView.Companion.SLIDE_DURATION
import com.djavid.smartsubs.models.SubscriptionPeriodType
import kotlinx.coroutines.*

class CreatePresenter(
    val view: CreateContract.View,
    coroutineScope: CoroutineScope
) : CreateContract.Presenter, CoroutineScope by coroutineScope {

    private val periodItems = SubscriptionPeriodType.values().toList()
    private var selectedPeriod = SubscriptionPeriodType.MONTH

    override fun init() {
        view.init(this)

        val periods = periodItems.map { view.getPeriodString(it) }
        view.setupSpinner(periods)
        view.selectPeriodItem(periodItems.indexOf(selectedPeriod))

        view.expandPanel()
        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)
    }

    override fun onItemSelected(position: Int) {
        selectedPeriod = periodItems[position]
    }

    override fun onCancelPressed() {
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun finish() {
        launch {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, SLIDE_DURATION)
            view.setBackgroundTransparent(true, SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(SLIDE_DURATION) }
            view.goBack()
        }
    }

}