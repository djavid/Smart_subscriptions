package com.djavid.smartsubs.subscription

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.djavid.features.subscription.databinding.FragmentSubscriptionBinding
import com.djavid.smartsubs.common.navigation.CommonFragmentNavigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubscriptionModule(fragment: Fragment, binding: ViewBinding) {
    val di = DI.Module("subscription_module") {
        bind<FragmentSubscriptionBinding>() with singleton { binding as FragmentSubscriptionBinding }
        bind<SubscriptionContract.Presenter>() with singleton {
            SubscriptionPresenter(
                instance(), instance(), instance(), instance(), instance(), instance(),
                instance(), instance(), instance(), instance() ,instance()
            )
        }
        bind<SubscriptionContract.View>() with singleton { SubscriptionView(instance()) }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<CommonFragmentNavigator>() with singleton {
            CommonFragmentNavigator(instance())
        }
    }
}