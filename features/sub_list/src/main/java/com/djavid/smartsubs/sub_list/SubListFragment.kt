package com.djavid.smartsubs.sub_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.djavid.core.ui.databinding.FragmentSubListBinding
import com.djavid.smartsubs.common.base.BackPressListener
import com.djavid.smartsubs.common.base.BaseFragment
import com.djavid.smartsubs.common.SmartSubsApplication
import org.kodein.di.instance

class SubListFragment : BaseFragment(), BackPressListener {

    private var _binding: FragmentSubListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val subListView: SubListContract.View by instance()

    private val presenter: SubListContract.Presenter by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentSubListBinding.inflate(inflater).apply {
            _binding = this
            di = (requireActivity().application as SmartSubsApplication).subListComponent(this@SubListFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        subListView.init(presenter)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subListView.destroy()
        _binding = null
    }

}