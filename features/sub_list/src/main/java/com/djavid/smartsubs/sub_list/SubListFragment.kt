package com.djavid.smartsubs.sub_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.djavid.core.ui.databinding.FragmentSubListBinding
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.common.BaseFragment
import com.djavid.smartsubs.common.SmartSubsApplication
import org.kodein.di.instance

class SubListFragment : BaseFragment(), BackPressListener {

    private lateinit var binding: FragmentSubListBinding
    private val presenter: SubListContract.Presenter by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSubListBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as SmartSubsApplication).subListComponent(this@SubListFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        presenter.init()
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

}