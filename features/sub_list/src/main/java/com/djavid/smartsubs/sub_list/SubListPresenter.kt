package com.djavid.smartsubs.sub_list

import android.os.Bundle
import androidx.lifecycle.LifecycleCoroutineScope
import com.djavid.smartsubs.common.navigation.CommonFragmentNavigator
import com.djavid.smartsubs.common.domain.PredefinedSubscription
import com.djavid.smartsubs.common.utils.Constants
import kotlinx.coroutines.*

class SubListPresenter(
    private val view: SubListContract.View,
    private val fragmentNavigator: CommonFragmentNavigator,
    lifecycleCoroutineScope: LifecycleCoroutineScope
) : SubListContract.Presenter, CoroutineScope by lifecycleCoroutineScope {

    override fun onItemClick(item: PredefinedSubscription) {
        fragmentNavigator.setFragmentResult(SubListContract.REQUEST_KEY, Bundle().apply {
            putSerializable(SubListContract.FRAGMENT_RESULT_KEY, item)
        })
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun finish() {
        launch(Dispatchers.Main) {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, Constants.SLIDE_DURATION)
            view.setBackgroundTransparent(true, Constants.SLIDE_DURATION)
            delay(Constants.SLIDE_DURATION)
            fragmentNavigator.goBack()
        }
    }
}