package com.djavid.smartsubs.home

import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R

class HomeNavigator(
    private val fragmentManager: FragmentManager
) : HomeContract.Navigator {

    override fun goToHome() {
        fragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainer, HomeFragment(), HomeFragment::class.java.simpleName)
            .commit()
    }

}