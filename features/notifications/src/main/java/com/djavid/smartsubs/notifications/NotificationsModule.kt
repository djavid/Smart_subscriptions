package com.djavid.smartsubs.notifications

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.djavid.features.notifications.databinding.FragmentNotificationsBinding
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class NotificationsModule(fragment: Fragment, binding: ViewBinding) {
    val di = DI.Module("notifications_module") {
        bind<Fragment>() with singleton { fragment }
        bind<Context>() with singleton { fragment.requireContext() }
        bind<FragmentNotificationsBinding>() with singleton { binding as FragmentNotificationsBinding }
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