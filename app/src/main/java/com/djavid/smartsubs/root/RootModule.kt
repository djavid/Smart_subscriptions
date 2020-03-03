package com.djavid.smartsubs.root

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_root.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class RootModule(activity: AppCompatActivity) {
    val kodein = Kodein.Module("root_module") {
        bind<View>() with singleton { activity.rootActivity }
        bind<RootContract.View>() with singleton {
            RootView(
                instance()
            )
        }
        bind<RootContract.Presenter>() with singleton {
            RootPresenter(
                instance(),
                instance()
            )
        }
        bind<FragmentManager>() with singleton { activity.supportFragmentManager }
    }
}