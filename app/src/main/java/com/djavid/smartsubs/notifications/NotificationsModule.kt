package com.djavid.smartsubs.notifications

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.databinding.FragmentNotificationsBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class NotificationsModule(fragment: Fragment, binding: FragmentNotificationsBinding) {
    val kodein = Kodein.Module("notifications_module") {
        bind<Fragment>() with singleton { fragment }
        bind<Context>() with singleton { fragment.requireContext() }
        bind<FragmentNotificationsBinding>() with singleton { binding }
        bind<NotificationsContract.View>() with singleton {
            NotificationsView(instance())
        }
        bind<NotificationsContract.Presenter>() with singleton {
            NotificationsPresenter(
                instance(), instance(), instance(), instance(), instance(), instance(), instance()
            )
        }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
    }
}