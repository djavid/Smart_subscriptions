package com.djavid.smartsubs.currency_list

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.djavid.features.currency_list.databinding.FragmentCurrencyListBinding
import com.djavid.smartsubs.common.CommonFragmentNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class CurrencyListModule(fragment: Fragment, binding: ViewBinding) {
    val di = DI.Module("currency_list_module") {
        bind<FragmentCurrencyListBinding>() with singleton { binding as FragmentCurrencyListBinding }
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