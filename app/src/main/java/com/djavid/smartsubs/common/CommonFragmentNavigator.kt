package com.djavid.smartsubs.common

import androidx.fragment.app.FragmentManager

class CommonFragmentNavigator(
    private val fragmentManager: FragmentManager
) {

    fun goBack() {
        fragmentManager.popBackStack()
    }

}