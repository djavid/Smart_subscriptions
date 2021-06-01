package com.djavid.smartsubs.sub_list

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.common.BaseFragment
import org.kodein.di.generic.instance

class SubListFragment : BaseFragment(R.layout.fragment_sub_list), BackPressListener {

    private val presenter: SubListContract.Presenter by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        kodein = (activity?.application as Application).subListComponent(this)

        presenter.init()
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

}