package com.djavid.smartsubs.home

import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R

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