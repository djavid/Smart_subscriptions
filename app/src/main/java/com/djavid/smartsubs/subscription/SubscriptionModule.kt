package com.djavid.smartsubs.subscription

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.databinding.FragmentSubscriptionBinding
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SubscriptionModule(fragment: Fragment, binding: FragmentSubscriptionBinding) {
    val di = DI.Module("subscription_module") {
        bind<FragmentSubscriptionBinding>() with singleton { binding }
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