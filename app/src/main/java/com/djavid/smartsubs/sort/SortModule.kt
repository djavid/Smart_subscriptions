package com.djavid.smartsubs.sort

import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sort.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class SortModule(fragment: Fragment) {
    val kodein = Kodein.Module("sort_module") {
        bind<Fragment>() with singleton { fragment }
        bind<SortContract.Presenter>() with singleton { SortPresenter(instance()) }
        bind<SortContract.View>() with singleton { SortView(instance()) }
        bind<View>() with singleton { fragment.sort_fragment }
    }
}