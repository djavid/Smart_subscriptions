package com.djavid.smartsubs.sub_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.djavid.core.ui.databinding.FragmentSubListBinding
import com.djavid.smartsubs.common.base.BaseFragment
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.utils.show
import kotlinx.coroutines.launch
import org.kodein.di.instance

class SubListFragment : BaseFragment() {

    private var _binding: FragmentSubListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val subListView: SubListContract.View by instance()

    private val presenter: SubListContract.Presenter by instance()
    private val viewModel: SubListViewModel by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentSubListBinding.inflate(inflater).apply {
            _binding = this
            di = (requireActivity().application as SmartSubsApplication).subListComponent(this@SubListFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        subListView.init(presenter)
        setupObservers()

        requireActivity().onBackPressedDispatcher.addCallback {
            presenter.goBack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subListView.destroy()
        _binding = null
    }

    private fun setupObservers() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.predefinedSubsFlow.collect {
                binding.subListProgress.show(false)
                (binding.subListRecycler.adapter as? SubsListAdapter)?.showSubs(it)
            }
        }
    }
}