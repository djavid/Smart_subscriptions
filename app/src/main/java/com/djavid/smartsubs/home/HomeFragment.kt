package com.djavid.smartsubs.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

class HomeFragment : Fragment(R.layout.fragment_home), KodeinAware {

    private val coroutineScope: CoroutineScope by instance()

    private lateinit var presenter: HomeContract.Presenter

    override lateinit var kodein: Kodein


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        kodein = (activity?.application as Application).homeComponent(this)
        presenter = kodein.direct.instance()

        presenter.init()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }

}