package com.djavid.smartsubs.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.djavid.features.create.databinding.FragmentCreateBinding
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.common.BaseFragment
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import com.djavid.smartsubs.utils.Constants
import com.djavid.smartsubs.utils.serializable
import org.kodein.di.instance

class CreateFragment : BaseFragment(), BackPressListener {

    private val presenter: CreateContract.Presenter by instance()
    private lateinit var binding: FragmentCreateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCreateBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as SmartSubsApplication).createComponent(this@CreateFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        arguments?.let {
            val subId = it.getString(Constants.KEY_SUBSCRIPTION_ID)
            presenter.init(subId)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            Constants.REQUEST_KEY, viewLifecycleOwner
        ) { _, result ->
            result.serializable<PredefinedSuggestionItem>(Constants.FRAGMENT_RESULT_KEY)?.let {
                presenter.onSuggestionItemClick(it)
            }
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

}