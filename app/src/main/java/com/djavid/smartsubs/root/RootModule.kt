package com.djavid.smartsubs.root

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.djavid.core.ui.databinding.ActivityRootBinding
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class RootModule(activity: AppCompatActivity, binding: ViewBinding) {
    val di = DI.Module("root_module") {
        bind<ActivityRootBinding>() with singleton { binding as ActivityRootBinding }
        bind<RootContract.View>() with singleton {
            RootView(instance())
        }
        bind<RootContract.Presenter>() with singleton {
            RootPresenter(
                instance(), instance(), instance(), instance(), instance(), instance()
            )
        }
        bind<FragmentManager>() with singleton { activity.supportFragmentManager }
    }
}