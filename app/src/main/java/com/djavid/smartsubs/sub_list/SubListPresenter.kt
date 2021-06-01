package com.djavid.smartsubs.sub_list

import androidx.lifecycle.LifecycleCoroutineScope
import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.smartsubs.storage.RealTimeRepository
import com.djavid.smartsubs.utils.SLIDE_DURATION
import kotlinx.coroutines.*

class SubListPresenter(
    private val view: SubListContract.View,
    private val realTimeRepository: RealTimeRepository,
    private val fragmentNavigator: CommonFragmentNavigator,
    private val pipeline: BasePipeline<PredefinedSuggestionItem>,
    lifecycleCoroutineScope: LifecycleCoroutineScope
) : SubListContract.Presenter, CoroutineScope by lifecycleCoroutineScope {

    private val predefinedSubs = mutableListOf<PredefinedSuggestionItem>()

    override fun init() {
        view.init(this)

        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)
        view.expandPanel()

        launch {
            loadPredefinedSubs()
        }
    }

    override fun onItemClick(item: PredefinedSuggestionItem) {
        pipeline.postValue(item)
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun loadPredefinedSubs() = launch(Dispatchers.Main) {
        predefinedSubs.clear()
        predefinedSubs.addAll(realTimeRepository.getAllPredefinedSubsWithLogo())
        view.showProgress(false)
        view.showPredefinedSubs(predefinedSubs)
    }

    private fun finish() {
        launch(Dispatchers.Main) {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, SLIDE_DURATION)
            view.setBackgroundTransparent(true, SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(SLIDE_DURATION) }
            fragmentNavigator.goBack()
        }
    }

}