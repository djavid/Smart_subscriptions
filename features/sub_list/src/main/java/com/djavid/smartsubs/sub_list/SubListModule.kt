package com.djavid.smartsubs.sub_list

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.djavid.common.CommonFragmentNavigator
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
        bind<com.djavid.common.CommonFragmentNavigator>() with singleton {
            com.djavid.common.CommonFragmentNavigator(fragment.requireActivity().supportFragmentManager)
        }
    }
}