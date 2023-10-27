package com.djavid.smartsubs.subscribe_media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.smartsubs.Application
import com.djavid.common.BaseBottomSheetFragment
import com.djavid.smartsubs.databinding.DialogSubscribeMediaBinding
import com.djavid.common.setWhiteNavigationBar
import org.kodein.di.instance

class SubscribeMediaDialog : BaseBottomSheetFragment() {

    private lateinit var binding: DialogSubscribeMediaBinding
    private val presenter: SubscribeMediaContract.Presenter by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DialogSubscribeMediaBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as Application).subscribeMediaComponent(this@SubscribeMediaDialog, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.init()
        dialog?.window?.setWhiteNavigationBar()
    }

}