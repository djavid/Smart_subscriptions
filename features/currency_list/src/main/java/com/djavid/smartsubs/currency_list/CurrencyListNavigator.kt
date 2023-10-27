package com.djavid.smartsubs.currency_list

import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.currency_list.R
import com.djavid.smartsubs.currency_list.CurrencyListContract

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