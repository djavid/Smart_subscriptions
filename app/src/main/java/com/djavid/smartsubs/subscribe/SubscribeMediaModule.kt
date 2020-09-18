package com.djavid.smartsubs.subscribe

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_subscribe_media.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SubscribeMediaModule(fragment: Fragment) {
    val kodein = Kodein.Module("subscribe_media_module") {
        bind<Fragment>() with singleton { fragment }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<SubscribeMediaContract.Presenter>() with singleton {
            SubscribeMediaPresenter(instance(), instance(), instance())
        }
        bind<SubscribeMediaContract.View>() with singleton {
            SubscribeMediaView(instance(), instance())
        }
        bind<View>() with singleton { fragment.subMedia_dialog }
    }
}