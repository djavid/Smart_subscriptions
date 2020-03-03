package com.djavid.smartsubs.root

import com.djavid.smartsubs.home.HomeContract

class RootPresenter(
    private val view: RootContract.View,
    private val homeNavigator: HomeContract.Navigator
) : RootContract.Presenter {

    override fun init() {
        view.init(this)
        goToHomeScreen()
    }

    override fun goToHomeScreen() {
        homeNavigator.goToHome()
    }

}