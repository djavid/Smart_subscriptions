package com.djavid.smartsubs.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.djavid.features.create.databinding.FragmentCreateBinding
import com.djavid.smartsubs.common.base.BaseFragment
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.domain.PredefinedSubscription
import com.djavid.smartsubs.common.utils.Constants
import kotlinx.coroutines.launch
import org.kodein.di.direct
import org.kodein.di.instance

class CreateFragment : BaseFragment() {

    private var presenter: CreateContract.Presenter? = null
    private val viewModel: CreateViewModel by instance()
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentCreateBinding.inflate(inflater).apply {
            _binding = this
            di = (requireActivity().application as SmartSubsApplication).createComponent(this@CreateFragment, binding)
            presenter = di.direct.instance()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setupObservers()

        arguments?.let {
            val subId = it.getString(Constants.KEY_SUBSCRIPTION_ID)
            presenter?.init(subId)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            Constants.SUBLIST_REQUEST_KEY, viewLifecycleOwner
        ) { _, result ->
            result.getString(Constants.SUBLIST_FRAGMENT_RESULT_KEY)?.let {
                presenter?.onPredefinedSubChosen(it)
            }
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) { presenter?.goBack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter = null
        _binding = null
    }

    private fun setupObservers() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.predefinedSubsFlow.collect { setupSuggestions(it) }
        }
    }

    private fun setupSuggestions(items: List<PredefinedSubscription>) {
        val adapter = SuggestionsAdapter(items.toMutableList(), binding.root.context)
        binding.createTitleInput.setAdapter(adapter)
        binding.createTitleInput.setOnItemClickListener { _, _, position, _ ->
            presenter?.onSuggestionItemClick(items[position])
        }
    }

}