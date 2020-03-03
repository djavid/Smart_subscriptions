package com.djavid.smartsubs.home

import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R

class HomeNavigator(
    private val fragmentManager: FragmentManager
) : HomeContract.Navigator {

    override fun goToHome() {
        fragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,
                HomeFragment()
            )
            .commit()
    }

}