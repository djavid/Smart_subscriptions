package com.djavid.smartsubs.create

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.common.CommonFragmentNavigator
import kotlinx.android.synthetic.main.fragment_create.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class CreateModule(fragment: Fragment) {
    val kodein = Kodein.Module("create_module") {
        bind<View>() with singleton { fragment.create_fragment }
        bind<CreateContract.Presenter>() with singleton {
            CreatePresenter(
                instance(), instance(), instance(), instance(), instance(), instance(),
                instance(), instance(), instance()
            )
        }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<CreateContract.View>() with singleton { CreateView(instance()) }
        bind<CommonFragmentNavigator>() with singleton {
            CommonFragmentNavigator(instance())
        }
    }
}