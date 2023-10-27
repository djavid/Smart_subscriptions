package com.djavid.smartsubs.create

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.FragmentCreateBinding
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class CreateModule(
    fragment: Fragment,
    binding: FragmentCreateBinding
) {
    val di = DI.Module("create_module") {
        bind<FragmentCreateBinding>() with singleton { binding }
        bind<CreateContract.Presenter>() with singleton {
            CreatePresenter(
                instance(), instance(), instance(), instance(), instance(), instance(),
                instance(), instance(), instance()
            )
        }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<CreateContract.View>() with singleton { CreateView(instance()) }
        bind<com.djavid.common.CommonFragmentNavigator>() with singleton {
            com.djavid.common.CommonFragmentNavigator(instance())
        }
    }
}