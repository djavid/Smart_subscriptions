package com.djavid.smartsubs.home

import androidx.fragment.app.FragmentManager
import com.djavid.core.ui.R
import com.djavid.smartsubs.common.HomeNavigator

class HomeNavigatorImpl(
    private val fragmentManager: FragmentManager
) : HomeNavigator {

    override fun goToHome() {
        fragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainer, HomeFragment(), HomeFragment::class.java.simpleName)
            .commit()
    }

}