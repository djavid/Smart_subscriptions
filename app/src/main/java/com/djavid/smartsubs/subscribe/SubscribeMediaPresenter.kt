package com.djavid.smartsubs.subscribe

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.storage.SharedRepository

class SubscribeMediaPresenter(
    private val view: SubscribeMediaContract.View,
    private val sharedPrefs: SharedRepository,
    private val firebaseLogger: FirebaseLogger
) : SubscribeMediaContract.Presenter {

    companion object {
        private const val TG_URL = "https://www.t.me/smartsubs"
    }

    override fun init() {
        view.init(this)
        firebaseLogger.subscribeTgShown()
        sharedPrefs.tgDialogTimesShown++
    }

    override fun onNoClicked() {
        firebaseLogger.subscribeTgClickedNo()
        view.close()
    }

    override fun onSubscribeClicked() {
        sharedPrefs.tgDialogTimesYesClicked++
        firebaseLogger.subscribeTgClickedYes()
        view.openTgChannelInApp()
    }

    override fun tgAppOpenFailed() {
        view.openTgChannelInBrowser(TG_URL)
    }

    override fun tgBrowserOpenFailed() {
        //no-op
    }

}