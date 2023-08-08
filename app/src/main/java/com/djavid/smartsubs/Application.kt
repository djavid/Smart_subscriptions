package com.djavid.smartsubs

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.Configuration
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.coroutines.CoroutineModule
import com.djavid.smartsubs.create.CreateModule
import com.djavid.smartsubs.create.CreateNavigatorModule
import com.djavid.smartsubs.currencyList.CurrencyListModule
import com.djavid.smartsubs.currencyList.CurrencyListNavigatorModule
import com.djavid.smartsubs.databinding.ActivityRootBinding
import com.djavid.smartsubs.databinding.DialogSubscribeMediaBinding
import com.djavid.smartsubs.databinding.FragmentCreateBinding
import com.djavid.smartsubs.databinding.FragmentCurrencyListBinding
import com.djavid.smartsubs.databinding.FragmentHomeBinding
import com.djavid.smartsubs.databinding.FragmentNotificationBinding
import com.djavid.smartsubs.databinding.FragmentNotificationsBinding
import com.djavid.smartsubs.databinding.FragmentSortBinding
import com.djavid.smartsubs.databinding.FragmentSubListBinding
import com.djavid.smartsubs.databinding.FragmentSubscriptionBinding
import com.djavid.smartsubs.db.DatabaseModule
import com.djavid.smartsubs.home.HomeModule
import com.djavid.smartsubs.home.HomeNavigatorModule
import com.djavid.smartsubs.interactors.CommonInteractorsModule
import com.djavid.smartsubs.mappers.MappersModule
import com.djavid.smartsubs.notification.AlarmNotifierModule
import com.djavid.smartsubs.notification.NotificationModule
import com.djavid.smartsubs.notification.NotificationNavigatorModule
import com.djavid.smartsubs.notifications.NotificationsModule
import com.djavid.smartsubs.notifications.NotificationsNavigatorModule
import com.djavid.smartsubs.root.FirebaseAuthHelper
import com.djavid.smartsubs.root.RootModule
import com.djavid.smartsubs.sort.SortModule
import com.djavid.smartsubs.sort.SortNavigationModule
import com.djavid.smartsubs.subList.SubListModule
import com.djavid.smartsubs.subList.SubListNavigatorModule
import com.djavid.smartsubs.subscribe.SubscribeMediaModule
import com.djavid.smartsubs.subscribe.SubscribeMediaNavigationModule
import com.djavid.smartsubs.subscription.SubscriptionModule
import com.djavid.smartsubs.subscription.SubscriptionNavigatorModule
import com.djavid.smartsubs.worker.NotificationWorkerModule
import com.google.firebase.FirebaseApp
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class Application : Application(), Configuration.Provider, DIAware {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initAppMetrica()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }

    private fun initAppMetrica() {
        if (!BuildConfig.DEBUG) {
            val apiKey = applicationContext.getString(R.string.yandex_metrica_api_key)
            val config = YandexMetricaConfig.newConfigBuilder(apiKey).build()
            YandexMetrica.activate(applicationContext, config)
            YandexMetrica.enableActivityAutoTracking(this)
        }
    }

    override val di by DI.lazy {
        bind<Application>() with singleton { this@Application }
        bind<Context>("appContext") with singleton { applicationContext }

        //firebase
        bind<FirebaseLogger>() with singleton { FirebaseLogger(instance("appContext")) }
        bind<FirebaseAuthHelper>() with singleton { FirebaseAuthHelper(instance(), instance()) }

        import(CoroutineModule().di)
        import(DatabaseModule(applicationContext).di)
        import(MappersModule().di)
        import(CommonInteractorsModule().di)
    }

    fun rootComponent(activity: AppCompatActivity, binding: ActivityRootBinding) = DI.lazy {
        extend(di)
        import(RootModule(activity, binding).di)
        import(HomeNavigatorModule().di)
        import(SubscriptionNavigatorModule().di)
    }

    fun homeComponent(fragment: Fragment, binding: FragmentHomeBinding) = DI.lazy {
        extend(di)
        import(HomeModule(fragment, binding).di)
        import(CreateNavigatorModule().di)
        import(SubscriptionNavigatorModule().di)
        import(SortNavigationModule().di)
        import(SubscribeMediaNavigationModule().di)
    }

    fun createComponent(fragment: Fragment, binding: FragmentCreateBinding) = DI.lazy {
        extend(di)
        import(CreateModule(fragment, binding).di)
        import(SubListNavigatorModule().di)
        import(CurrencyListNavigatorModule().di)
    }

    fun subscriptionComponent(fragment: Fragment, binding: FragmentSubscriptionBinding) = DI.lazy {
        extend(di)
        import(SubscriptionModule(fragment, binding).di)
        import(CreateNavigatorModule().di)
        import(HomeNavigatorModule().di)
        import(AlarmNotifierModule().di)
        import(NotificationsNavigatorModule().di)
    }

    fun notificationComponent(fragment: Fragment, binding: FragmentNotificationBinding) = DI.lazy {
        extend(di)
        import(NotificationModule(fragment, binding).di)
        import(AlarmNotifierModule().di)
    }

    fun notificationsComponent(fragment: Fragment, binding: FragmentNotificationsBinding) = DI.lazy {
        extend(di)
        import(NotificationNavigatorModule().di)
        import(NotificationsModule(fragment, binding).di)
        import(AlarmNotifierModule().di)
    }

    fun sortComponent(fragment: Fragment, binding: FragmentSortBinding) = DI.lazy {
        extend(di)
        import(SortModule(fragment, binding).di)
        import(SortNavigationModule().di)
    }

    fun subscribeMediaComponent(fragment: Fragment, binding: DialogSubscribeMediaBinding) = DI.lazy {
        extend(di)
        import(SubscribeMediaModule(fragment, binding).di)
    }

    fun subListComponent(fragment: Fragment, binding: FragmentSubListBinding) = DI.lazy {
        extend(di)
        import(SubListModule(fragment, binding).di)
    }

    fun currencyListComponent(fragment: Fragment, binding: FragmentCurrencyListBinding) = DI.lazy {
        extend(di)
        import(CurrencyListModule(fragment, binding).di)
    }

    fun notificationWorkerComponent(context: Context) = DI.lazy {
        extend(di)
        import(NotificationWorkerModule(context).di)
        import(AlarmNotifierModule().di)
    }

    fun uploaderComponent() = DI.lazy {
        extend(di)
    }

}