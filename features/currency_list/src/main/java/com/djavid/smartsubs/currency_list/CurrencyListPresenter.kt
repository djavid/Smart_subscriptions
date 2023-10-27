package com.djavid.smartsubs.currency_list

import android.os.Bundle
import androidx.lifecycle.LifecycleCoroutineScope
import com.djavid.common.CommonFragmentNavigator
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.data.storage.RealTimeRepository
import com.djavid.common.SLIDE_DURATION
import com.djavid.smartsubs.currency_list.CurrencyListContract
import kotlinx.coroutines.*

class CurrencyListPresenter(
    private val view: CurrencyListContract.View,
    private val realTimeRepository: com.djavid.data.storage.RealTimeRepository,
    private val fragmentNavigator: com.djavid.common.CommonFragmentNavigator,
    lifecycleCoroutineScope: LifecycleCoroutineScope
) : CurrencyListContract.Presenter, CoroutineScope by lifecycleCoroutineScope {

    private val predefinedSubs = mutableListOf<PredefinedSuggestionItem>()

    override fun init() {
        view.init(this)

        view.setBackgroundTransparent(false, com.djavid.common.SLIDE_DURATION)
        view.showToolbar(true, com.djavid.common.SLIDE_DURATION)
        view.expandPanel()

        launch {
            loadCurrencies()
        }
    }

    override fun onItemClick(item: PredefinedSuggestionItem) {
        fragmentNavigator.setFragmentResult(CurrencyListContract.REQUEST_KEY, Bundle().apply {
            putSerializable(CurrencyListContract.FRAGMENT_RESULT_KEY, item)
        })
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun loadCurrencies() {
        //todo
    }

//    private fun loadPredefinedSubs() = launch(Dispatchers.Main) {
//        predefinedSubs.clear()
//        predefinedSubs.addAll(realTimeRepository.getAllPredefinedSubsWithLogo())
//        view.showProgress(false)
//        view.showPredefinedSubs(predefinedSubs)
//    }

    private fun finish() {
        launch(Dispatchers.Main) {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, com.djavid.common.SLIDE_DURATION)
            view.setBackgroundTransparent(true, com.djavid.common.SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(com.djavid.common.SLIDE_DURATION) }
            fragmentNavigator.goBack()
        }
    }


}