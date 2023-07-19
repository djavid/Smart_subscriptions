package com.djavid.smartsubs.notification

import android.content.Context
import androidx.fragment.app.Fragment
import com.djavid.smartsubs.databinding.FragmentNotificationBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class NotificationModule(fragment: Fragment, binding: FragmentNotificationBinding) {
    val kodein = Kodein.Module("notification_module") {
        bind<Fragment>() with singleton { fragment }
        bind<Context>() with singleton { fragment.requireContext() }
        bind<FragmentNotificationBinding>() with singleton { binding }
        bind<NotificationContract.View>() with singleton {
            NotificationView(instance(), instance())
        }
        bind<NotificationContract.Presenter>() with singleton {
            NotificationPresenter(
                instance(), instance(), instance(), instance(), instance(), instance(), instance()
            )
        }
    }
}