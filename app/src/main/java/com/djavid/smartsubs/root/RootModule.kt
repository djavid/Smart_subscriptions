package com.djavid.smartsubs.root

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.databinding.ActivityRootBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class RootModule(activity: AppCompatActivity, binding: ActivityRootBinding) {
    val kodein = Kodein.Module("root_module") {
        bind<ActivityRootBinding>() with singleton { binding }
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