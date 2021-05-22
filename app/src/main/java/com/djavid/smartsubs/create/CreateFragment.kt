package com.djavid.smartsubs.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.common.BaseFragment
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class CreateFragment : BaseFragment(R.layout.fragment_create), BackPressListener {

    private val presenter: CreateContract.Presenter by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        kodein = (activity?.application as Application).createComponent(this)

        arguments?.let {
            val subId = it.getString(KEY_SUBSCRIPTION_ID)
            presenter.init(subId)
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

}