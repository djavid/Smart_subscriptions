package com.djavid.smartsubs.currencyList

import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R

class CurrencyListNavigator(
    private val fm: FragmentManager
): CurrencyListContract.Navigator {

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