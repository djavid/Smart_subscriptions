package com.djavid.smartsubs.subscribe

import com.djavid.smartsubs.utils.FirebaseLogger
import com.djavid.smartsubs.utils.SharedRepository

class SubscribeMediaPresenter(
    private val view: SubscribeMediaContract.View,
    private val sharedPrefs: SharedRepository,
    private val firebaseLogger: FirebaseLogger
) : SubscribeMediaContract.Presenter {

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
        view.openTgChannel()
    }
}