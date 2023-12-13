package com.djavid.smartsubs.sort

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SortModule(fragment: Fragment) {
    val di = DI.Module("sort_module") {
        bind<Fragment>() with singleton { fragment }
        bind<FragmentManager>() with singleton { fragment.childFragmentManager }
        bind<SortViewModel>() with singleton {
            SortViewModelImpl(
                instance(), instance(), instance(), instance()
            )
        }
    }
}