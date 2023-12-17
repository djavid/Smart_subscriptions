package com.djavid.smartsubs.create

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.djavid.features.create.databinding.FragmentCreateBinding
import com.djavid.smartsubs.common.navigation.CommonFragmentNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class CreateModule(
    fragment: Fragment,
    binding: ViewBinding
) {
    val di = DI.Module("create_module") {
        bind<FragmentCreateBinding>() with singleton { binding as FragmentCreateBinding }
        bind<CreateContract.Presenter>() with singleton {
            CreatePresenter(
                instance(), instance(), instance(), instance(), instance(), instance(),
                instance(), instance(), instance()
            )
        }
        bind<LifecycleCoroutineScope>() with singleton { fragment.viewLifecycleOwner.lifecycleScope }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<CreateViewModel>() with singleton { CreateViewModelImpl(instance()) }
        bind<CreateContract.View>() with singleton { CreateView(instance(), instance(), instance()) }
        bind<CommonFragmentNavigator>() with singleton {
            CommonFragmentNavigator(instance())
        }
    }
}