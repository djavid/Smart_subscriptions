package com.djavid.smartsubs.subscription

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.common.CommonFragmentNavigator
import kotlinx.android.synthetic.main.fragment_subscription.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SubscriptionModule(fragment: Fragment) {
    val kodein = Kodein.Module("subscription_module") {
        bind<View>() with singleton { fragment.sub_fragment }
        bind<SubscriptionContract.Presenter>() with singleton {
            SubscriptionPresenter(
                instance(), instance(), instance(), instance(), instance(), instance(),
                instance(), instance(), instance()
            )
        }
        bind<SubscriptionContract.View>() with singleton { SubscriptionView(instance()) }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<CommonFragmentNavigator>() with singleton {
            CommonFragmentNavigator(instance())
        }
    }
}