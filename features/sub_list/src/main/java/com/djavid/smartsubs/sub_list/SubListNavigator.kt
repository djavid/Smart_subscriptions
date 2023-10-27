package com.djavid.smartsubs.sub_list

import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R

class SubListNavigator(
    private val fm: FragmentManager
): SubListContract.Navigator {

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