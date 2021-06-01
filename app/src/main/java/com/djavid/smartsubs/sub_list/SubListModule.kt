package com.djavid.smartsubs.sub_list

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.djavid.smartsubs.common.CommonFragmentNavigator
import kotlinx.android.synthetic.main.fragment_sub_list.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SubListModule(fragment: Fragment) {
    val kodein = Kodein.Module("sub_list_module") {
        bind<View>() with singleton { fragment.subList_fragment }
        bind<SubListContract.Presenter>() with singleton {
            SubListPresenter(instance(), instance(), instance(), instance(), instance())
        }
        bind<LifecycleCoroutineScope>() with singleton { fragment.lifecycleScope }
        bind<SubListContract.View>() with singleton { SubListView(instance()) }
        bind<CommonFragmentNavigator>() with singleton {
            CommonFragmentNavigator(fragment.requireActivity().supportFragmentManager)
        }
    }
}