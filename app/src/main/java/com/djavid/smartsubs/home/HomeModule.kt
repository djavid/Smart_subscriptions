package com.djavid.smartsubs.home

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.djavid.smartsubs.databinding.FragmentHomeBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class HomeModule(fragment: Fragment, binding: FragmentHomeBinding) {
    val kodein = Kodein.Module("home_module") {
        bind<HomeContract.View>() with singleton {
            HomeView(instance())
        }
        bind<Activity>() with singleton { fragment.requireActivity() }
        bind<FragmentHomeBinding>() with singleton { binding }
        bind<LifecycleCoroutineScope>() with singleton { fragment.lifecycleScope }
        bind<HomeContract.Presenter>() with singleton {
            HomePresenter(
                instance(), instance(), instance(), instance(), instance(), instance(), instance(),
                instance(), instance(), instance(), instance(), instance()
            )
        }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
        bind<InAppReview>() with singleton {
            InAppReview(instance(), instance(), instance())
        }
    }
}