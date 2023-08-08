package com.djavid.smartsubs.subscribe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.databinding.DialogSubscribeMediaBinding
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubscribeMediaModule(fragment: Fragment, binding: DialogSubscribeMediaBinding) {
    val di = DI.Module("subscribe_media_module") {
        bind<Fragment>() with singleton { fragment }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<SubscribeMediaContract.Presenter>() with singleton {
            SubscribeMediaPresenter(instance(), instance(), instance(), instance())
        }
        bind<SubscribeMediaContract.View>() with singleton {
            SubscribeMediaView(instance(), instance())
        }
        bind<DialogSubscribeMediaBinding>() with singleton { binding }
    }
}