package com.djavid.smartsubs.home

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class HomeModule(fragment: Fragment) {
    val kodein = Kodein.Module("home_module") {
        bind<HomeContract.View>() with singleton {
            HomeView(instance())
        }
        bind<View>() with singleton { fragment.homeFragment }
        bind<HomeContract.Presenter>() with singleton {
            HomePresenter(instance(), instance(), instance(), instance(), instance())
        }
        bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }
    }
}