package com.djavid.smartsubs.notification

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_notification.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class NotificationModule(fragment: Fragment) {
    val kodein = Kodein.Module("notification_module") {
        bind<Fragment>() with singleton { fragment }
        bind<Context>() with singleton { fragment.requireContext() }
        bind<View>() with singleton { fragment.notif_fragment }
        bind<NotificationContract.View>() with singleton {
            NotificationView(instance(), instance())
        }
        bind<NotificationContract.Presenter>() with singleton {
            NotificationPresenter(
                instance(), instance(), instance(), instance(), instance(), instance()
            )
        }
    }
}