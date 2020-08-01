package com.djavid.smartsubs

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.djavid.smartsubs.coroutines.CoroutineModule
import com.djavid.smartsubs.create.CreateModule
import com.djavid.smartsubs.create.CreateNavigatorModule
import com.djavid.smartsubs.db.DatabaseModule
import com.djavid.smartsubs.home.HomeModule
import com.djavid.smartsubs.home.HomeNavigatorModule
import com.djavid.smartsubs.mappers.MappersModule
import com.djavid.smartsubs.notification.AlarmNotifierModule
import com.djavid.smartsubs.notification.NotificationModule
import com.djavid.smartsubs.notification.NotificationNavigatorModule
import com.djavid.smartsubs.worker.NotificationWorkerModule
import com.djavid.smartsubs.root.RootModule
import com.djavid.smartsubs.sort.SortModule
import com.djavid.smartsubs.sort.SortNavigationModule
import com.djavid.smartsubs.subscription.SubscriptionModule
import com.djavid.smartsubs.subscription.SubscriptionNavigatorModule
import net.danlew.android.joda.JodaTimeAndroid
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class Application : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }

    override val kodein = Kodein.lazy {
        bind<Application>() with singleton { this@Application }

        import(CoroutineModule().kodein)
        import(DatabaseModule(applicationContext).kodein)
        import(MappersModule().kodein)
    }

    fun rootComponent(activity: AppCompatActivity) = Kodein.lazy {
        extend(kodein)
        import(RootModule(activity).kodein)
        import(HomeNavigatorModule().kodein)
        import(SubscriptionNavigatorModule().kodein)
    }

    fun homeComponent(fragment: Fragment) = Kodein.lazy {
        extend(kodein)
        import(HomeModule(fragment).kodein)
        import(CreateNavigatorModule().kodein)
        import(SubscriptionNavigatorModule().kodein)
        import(SortNavigationModule().kodein)
    }

    fun createComponent(fragment: Fragment) = Kodein.lazy {
        extend(kodein)
        import(CreateModule(fragment).kodein)
    }

    fun subscriptionComponent(fragment: Fragment) = Kodein.lazy {
        extend(kodein)
        import(SubscriptionModule(fragment).kodein)
        import(CreateNavigatorModule().kodein)
        import(NotificationNavigatorModule().kodein)
        import(HomeNavigatorModule().kodein)
        import(AlarmNotifierModule().kodein)
    }

    fun notificationComponent(fragment: Fragment) = Kodein.lazy {
        extend(kodein)
        import(NotificationModule(fragment).kodein)
        import(AlarmNotifierModule().kodein)
    }

    fun notificationWorkerComponent(context: Context) = Kodein.lazy {
        extend(kodein)
        import(NotificationWorkerModule(context).kodein)
        import(AlarmNotifierModule().kodein)
    }

    fun sortComponent(fragment: Fragment) = Kodein.lazy {
        extend(kodein)
        import(SortModule(fragment).kodein)
        import(SortNavigationModule().kodein)
    }

}