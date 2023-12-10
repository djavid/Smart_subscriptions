package com.djavid.smartsubs.sub_list

import androidx.fragment.app.FragmentManager
import com.djavid.core.ui.R
import com.djavid.smartsubs.common.navigation.SubListNavigator

class SubListNavigatorImpl(
    private val fm: FragmentManager
): SubListNavigator {

    override fun goToSubListScreen() {
        fm.beginTransaction()
            .add(
                R.id.fragmentContainer,
                SubListFragment(),
                SubListFragment::class.java.simpleName
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

}