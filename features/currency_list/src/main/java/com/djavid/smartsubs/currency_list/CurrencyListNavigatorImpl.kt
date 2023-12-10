package com.djavid.smartsubs.currency_list

import androidx.fragment.app.FragmentManager
import com.djavid.core.ui.R
import com.djavid.smartsubs.common.navigation.CurrencyListNavigator

class CurrencyListNavigatorImpl(
    private val fm: FragmentManager
): CurrencyListNavigator {

    override fun goToCurrencyListScreen() {
        fm.beginTransaction()
            .add(
                R.id.fragmentContainer,
                CurrencyListFragment(),
                CurrencyListFragment::class.java.simpleName
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

}