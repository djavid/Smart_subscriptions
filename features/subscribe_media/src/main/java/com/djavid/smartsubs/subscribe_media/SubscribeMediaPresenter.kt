package com.djavid.smartsubs.subscribe_media

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.data.storage.SharedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubscribeMediaPresenter(
    private val view: SubscribeMediaContract.View,
    private val sharedPrefs: com.djavid.data.storage.SharedRepository,
    private val firebaseLogger: FirebaseLogger,
    coroutineScope: CoroutineScope
) : SubscribeMediaContract.Presenter, CoroutineScope by coroutineScope {

    companion object {
        private const val TG_URL = "https://www.t.me/smartsubs"
    }

    override fun init() {
        view.init(this)

        launch(Dispatchers.IO) {
            firebaseLogger.subscribeTgShown()
            sharedPrefs.tgDialogTimesShown++
        }
    }

    override fun onNoClicked() {
        launch { firebaseLogger.subscribeTgClickedNo() }
        view.close()
    }

    override fun onSubscribeClicked() {
        sharedPrefs.tgDialogTimesYesClicked++
        launch { firebaseLogger.subscribeTgClickedYes() }
        view.openTgChannelInApp()
    }

    override fun tgAppOpenFailed() {
        view.openTgChannelInBrowser(TG_URL)
    }

    override fun tgBrowserOpenFailed() {
        //no-op
    }

}