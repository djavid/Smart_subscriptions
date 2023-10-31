package com.djavid.smartsubs.home

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.djavid.features.home.databinding.FragmentHomeBinding
import com.djavid.smartsubs.data.InAppReview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class HomeModule(fragment: Fragment, binding: ViewBinding) {
    val di = DI.Module("home_module") {
        bind<HomeView>() with singleton {
            HomeView(instance(), instance())
        }
        bind<Activity>() with singleton { fragment.requireActivity() }
        bind<FragmentHomeBinding>() with singleton { binding as FragmentHomeBinding }
        bind<LifecycleCoroutineScope>() with singleton { fragment.lifecycleScope }
        bind<HomeViewModel>() with singleton {
            HomeViewModel(
                instance(), instance(), instance(), instance(), instance(), instance(),
                instance(), instance(), instance()
            )
        }
        bind<HomeInteractor>() with singleton {
            HomeInteractor(instance(), instance(), instance(), instance(), instance(), instance(), instance())
        }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<CoroutineDispatcher>() with singleton { Dispatchers.IO }
        bind<InAppReview>() with singleton {
            InAppReview(instance(), instance(), instance())
        }
    }
}