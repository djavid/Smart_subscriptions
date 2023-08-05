package com.djavid.smartsubs.currencyList

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.databinding.FragmentCurrencyListBinding
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class CurrencyListModule(fragment: Fragment, binding: FragmentCurrencyListBinding) {
    val di = DI.Module("currency_list_module") {
        bind<FragmentCurrencyListBinding>() with singleton { binding }
        bind<CurrencyListContract.Presenter>() with singleton {
            CurrencyListPresenter(instance(), instance(), instance(), instance())
        }
        bind<LifecycleCoroutineScope>() with singleton { fragment.lifecycleScope }
        bind<CurrencyListContract.View>() with singleton { CurrencyListView(instance()) }
        bind<CommonFragmentNavigator>() with singleton {
            CommonFragmentNavigator(fragment.requireActivity().supportFragmentManager)
        }
    }
}