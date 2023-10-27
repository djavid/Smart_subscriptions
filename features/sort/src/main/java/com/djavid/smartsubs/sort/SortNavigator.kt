package com.djavid.smartsubs.sort

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.common.KEY_SORT_SCREEN_TYPE

class SortNavigator(
    private val fm: FragmentManager
): SortContract.Navigator {

    override fun openSortScreen() {
        val fragment = SortFragment().apply {
            arguments = Bundle().apply {
                putString(com.djavid.common.KEY_SORT_SCREEN_TYPE, SortContract.ScreenType.SORT.name)
            }
        }

        fragment.show(fm, "sortScreen")
    }

    override fun openSortByScreen() {
        val fragment = SortFragment().apply {
            arguments = Bundle().apply {
                putString(com.djavid.common.KEY_SORT_SCREEN_TYPE, SortContract.ScreenType.SORT_BY.name)
            }
        }

        fragment.show(fm, "sortByScreen")
    }

}