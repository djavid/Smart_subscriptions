package com.djavid.smartsubs.sub_list

import android.os.Bundle
import androidx.lifecycle.LifecycleCoroutineScope
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import com.djavid.smartsubs.data.storage.RealTimeRepository
import com.djavid.smartsubs.utils.Constants
import kotlinx.coroutines.*

class SubListPresenter(
    private val view: SubListContract.View,
    private val realTimeRepository: RealTimeRepository,
    private val fragmentNavigator: CommonFragmentNavigator,
    lifecycleCoroutineScope: LifecycleCoroutineScope
) : SubListContract.Presenter, CoroutineScope by lifecycleCoroutineScope {

    private val predefinedSubs = mutableListOf<PredefinedSuggestionItem>()

    override fun init() {
        view.init(this)

        view.setBackgroundTransparent(false, Constants.SLIDE_DURATION)
        view.showToolbar(true, Constants.SLIDE_DURATION)
        view.expandPanel()

        launch {
            loadPredefinedSubs()
        }
    }

    override fun onItemClick(item: PredefinedSuggestionItem) {
        fragmentNavigator.setFragmentResult(SubListContract.REQUEST_KEY, Bundle().apply {
            putSerializable(SubListContract.FRAGMENT_RESULT_KEY, item)
        })
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun loadPredefinedSubs() = launch(Dispatchers.Main) {
        predefinedSubs.clear()
        predefinedSubs.addAll(realTimeRepository.getAllPredefinedSubsWithLogo(allowCache = true))
        view.showProgress(false)
        view.showPredefinedSubs(predefinedSubs)
    }

    private fun finish() {
        launch(Dispatchers.Main) {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, Constants.SLIDE_DURATION)
            view.setBackgroundTransparent(true, Constants.SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(Constants.SLIDE_DURATION) }
            fragmentNavigator.goBack()
        }
    }


}