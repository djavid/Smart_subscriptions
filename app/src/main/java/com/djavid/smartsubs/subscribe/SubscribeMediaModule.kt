package com.djavid.smartsubs.subscribe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.databinding.DialogSubscribeMediaBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SubscribeMediaModule(fragment: Fragment, binding: DialogSubscribeMediaBinding) {
    val kodein = Kodein.Module("subscribe_media_module") {
        bind<Fragment>() with singleton { fragment }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<SubscribeMediaContract.Presenter>() with singleton {
            SubscribeMediaPresenter(instance(), instance(), instance())
        }
        bind<SubscribeMediaContract.View>() with singleton {
            SubscribeMediaView(instance(), instance())
        }
        bind<DialogSubscribeMediaBinding>() with singleton { binding }
    }
}