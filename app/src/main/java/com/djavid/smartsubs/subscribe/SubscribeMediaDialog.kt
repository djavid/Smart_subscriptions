package com.djavid.smartsubs.subscribe

import android.os.Bundle
import android.view.View
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BaseBottomSheetFragment
import com.djavid.smartsubs.utils.setWhiteNavigationBar
import org.kodein.di.generic.instance

class SubscribeMediaDialog : BaseBottomSheetFragment(R.layout.dialog_subscribe_media) {

    private val presenter: SubscribeMediaContract.Presenter by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kodein = (activity?.application as Application).subscribeMediaComponent(this)

        presenter.init()
        dialog?.window?.setWhiteNavigationBar()
    }

}