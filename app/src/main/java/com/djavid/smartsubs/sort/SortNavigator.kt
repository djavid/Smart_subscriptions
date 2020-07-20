package com.djavid.smartsubs.sort

import androidx.fragment.app.FragmentManager

class SortNavigator(
    private val fm: FragmentManager
): SortContract.Navigator {

    override fun openSortScreen() {
        SortFragment().show(fm, "sortScreen")
    }
}