package com.djavid.smartsubs.subList

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.databinding.FragmentSubListBinding
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubListModule(fragment: Fragment, binding: FragmentSubListBinding) {
    val di = DI.Module("sub_list_module") {
        bind<FragmentSubListBinding>() with singleton { binding }
        bind<SubListContract.Presenter>() with singleton {
            SubListPresenter(instance(), instance(), instance(), instance())
        }
        bind<LifecycleCoroutineScope>() with singleton { fragment.lifecycleScope }
        bind<SubListContract.View>() with singleton { SubListView(instance()) }
        bind<CommonFragmentNavigator>() with singleton {
            CommonFragmentNavigator(fragment.requireActivity().supportFragmentManager)
        }
    }
}