package com.djavid.smartsubs.common

import android.os.Bundle
import androidx.fragment.app.FragmentManager

class CommonFragmentNavigator(
    private val fragmentManager: FragmentManager
) {

    fun goBack() {
        fragmentManager.popBackStack()
    }

    fun setFragmentResult(requestKey: String, result: Bundle) {
        fragmentManager.setFragmentResult(requestKey, result)
    }

}