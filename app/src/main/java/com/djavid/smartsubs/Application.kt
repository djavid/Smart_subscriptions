package com.djavid.smartsubs

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.work.Configuration
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.coroutines.CoroutineModule
import com.djavid.smartsubs.create.CreateModule
import com.djavid.smartsubs.create.CreateNavigatorModule
import com.djavid.smartsubs.currency_list.CurrencyListModule
import com.djavid.smartsubs.currency_list.CurrencyListNavigatorModule
import com.djavid.smartsubs.data.db.DatabaseModule
import com.djavid.smartsubs.home.HomeModule
import com.djavid.smartsubs.home.HomeNavigatorModule
import com.djavid.smartsubs.data.interactors.CommonInteractorsModule
import com.djavid.smartsubs.data.mappers.MappersModule
import com.djavid.smartsubs.alarm.AlarmNotifierModule
import com.djavid.smartsubs.notification.NotificationModule
import com.djavid.smartsubs.notification.NotificationNavigatorModule
import com.djavid.smartsubs.notifications.NotificationsModule
import com.djavid.smartsubs.notifications.NotificationsNavigatorModule
import com.djavid.smartsubs.data.FirebaseAuthHelper
import com.djavid.smartsubs.root.RootModule
import com.djavid.smartsubs.sort.SortModule
import com.djavid.smartsubs.sort.SortNavigationModule
import com.djavid.smartsubs.sub_list.SubListModule
import com.djavid.smartsubs.sub_list.SubListNavigatorModule
import com.djavid.smartsubs.subscribe_media.SubscribeMediaModule
import com.djavid.smartsubs.subscribe_media.SubscribeMediaNavigationModule
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

class Application : SmartSubsApplication(), Configuration.Provider, DIAware {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initAppMetrica()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    private fun initAppMetrica() {
        if (!BuildConfig.DEBUG) {
            val apiKey = applicationContext.getString(com.djavid.core.ui.R.string.yandex_metrica_api_key)
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

    override fun rootComponent(activity: AppCompatActivity, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(RootModule(activity, binding).di)
        import(HomeNavigatorModule().di)
        import(SubscriptionNavigatorModule().di)
    }

    override fun homeComponent(fragment: Fragment, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(HomeModule(fragment, binding).di)
        import(CreateNavigatorModule().di)
        import(SubscriptionNavigatorModule().di)
        import(SortNavigationModule().di)
        import(SubscribeMediaNavigationModule().di)
    }

    override fun createComponent(fragment: Fragment, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(CreateModule(fragment, binding).di)
        import(SubListNavigatorModule().di)
        import(CurrencyListNavigatorModule().di)
    }

    override fun subscriptionComponent(fragment: Fragment, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(SubscriptionModule(fragment, binding).di)
        import(CreateNavigatorModule().di)
        import(HomeNavigatorModule().di)
        import(AlarmNotifierModule().di)
        import(NotificationsNavigatorModule().di)
    }

    override fun notificationComponent(fragment: Fragment, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(NotificationModule(fragment, binding).di)
        import(AlarmNotifierModule().di)
    }

    override fun notificationsComponent(fragment: Fragment, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(NotificationNavigatorModule().di)
        import(NotificationsModule(fragment, binding).di)
        import(AlarmNotifierModule().di)
    }

    override fun sortComponent(fragment: Fragment) = DI.lazy {
        extend(di)
        import(SortModule(fragment).di)
        import(SortNavigationModule().di)
    }

    override fun subscribeMediaComponent(fragment: Fragment, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(SubscribeMediaModule(fragment, binding).di)
    }

    override fun subListComponent(fragment: Fragment, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(SubListModule(fragment, binding).di)
    }

    override fun currencyListComponent(fragment: Fragment, binding: ViewBinding) = DI.lazy {
        extend(di)
        import(CurrencyListModule(fragment, binding).di)
    }

    override fun notificationWorkerComponent(context: Context) = DI.lazy {
        extend(di)
        import(NotificationWorkerModule(context).di)
        import(AlarmNotifierModule().di)
    }

    override fun uploaderComponent() = DI.lazy {
        extend(di)
    }

}