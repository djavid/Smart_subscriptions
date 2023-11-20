package com.djavid.smartsubs.sort

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.common.SortNavigator
import com.djavid.smartsubs.common.utils.Constants.KEY_SORT_SCREEN_TYPE

class SortNavigatorImpl(
    private val fm: FragmentManager
): SortNavigator {

    override fun openSortScreen() {
        val fragment = SortFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_SORT_SCREEN_TYPE, SortContract.ScreenType.SORT.name)
            }
        }

        fragment.show(fm, fragment::class.java.name)
    }

    override fun openSortByScreen() {
        val fragment = SortFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_SORT_SCREEN_TYPE, SortContract.ScreenType.SORT_BY.name)
            }
        }

        fragment.show(fm, fragment::class.java.name)
    }

}