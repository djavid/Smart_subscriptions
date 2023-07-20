package com.djavid.smartsubs.sort

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.databinding.FragmentSortBinding
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SortModule(fragment: Fragment, binding: FragmentSortBinding) {
    val di = DI.Module("sort_module") {
        bind<Fragment>() with singleton { fragment }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<SortContract.Presenter>() with singleton {
            SortPresenter(
                instance(), instance(), instance(), instance(), instance(), instance()
            )
        }
        bind<SortContract.View>() with singleton { SortView(instance(), instance()) }
        bind<FragmentSortBinding>() with singleton { binding }
    }
}