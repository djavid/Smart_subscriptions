package com.djavid.smartsubs.common

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.kodein.di.DI

abstract class SmartSubsApplication: Application() {
    abstract fun rootComponent(activity: AppCompatActivity, binding: ViewBinding): DI
    abstract fun homeComponent(fragment: Fragment, binding: ViewBinding): DI
    abstract fun createComponent(fragment: Fragment, binding: ViewBinding): DI
    abstract fun subscriptionComponent(fragment: Fragment, binding: ViewBinding): DI
    abstract fun notificationComponent(fragment: Fragment, binding: ViewBinding): DI
    abstract fun notificationsComponent(fragment: Fragment, binding: ViewBinding): DI
    abstract fun sortComponent(fragment: Fragment): DI
    abstract fun subscribeMediaComponent(fragment: Fragment, binding: ViewBinding): DI
    abstract fun subListComponent(fragment: Fragment, binding: ViewBinding): DI
    abstract fun currencyListComponent(fragment: Fragment, binding: ViewBinding): DI
    abstract fun notificationWorkerComponent(context: Context): DI
    abstract fun uploaderComponent(): DI
}