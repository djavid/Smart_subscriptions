package com.djavid.smartsubs.sub_list

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.djavid.core.ui.databinding.FragmentSubListBinding
import com.djavid.smartsubs.common.navigation.CommonFragmentNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubListModule(fragment: Fragment, binding: ViewBinding) {
    val di = DI.Module("sub_list_module") {
        bind<FragmentSubListBinding>() with singleton { binding as FragmentSubListBinding }
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