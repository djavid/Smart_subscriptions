package com.djavid.smartsubs.sort

import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.common.navigation.SortNavigator

class SortNavigatorImpl(
    private val fm: FragmentManager
): SortNavigator {

    override fun openSortScreen() {
        val fragment = SortFragment()

        fm.beginTransaction()
            .add(fragment, fragment::class.java.name)
            .commit()
    }

    override fun openSortByScreen() {
        val fragment = SortByFragment()

        fm.beginTransaction()
            .addToBackStack(fragment::class.java.name)
            .setReorderingAllowed(true)
            .add(fragment, null)
            .commit()
    }

    override fun goBack() {
        fm.popBackStack()
    }
}