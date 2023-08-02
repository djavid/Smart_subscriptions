package com.djavid.smartsubs.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.common.BaseFragment
import com.djavid.smartsubs.databinding.FragmentCreateBinding
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.smartsubs.sub_list.SubListContract
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID
import com.djavid.smartsubs.utils.serializable
import org.kodein.di.instance

class CreateFragment : BaseFragment(), BackPressListener {

    private val presenter: CreateContract.Presenter by instance()
    private lateinit var binding: FragmentCreateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCreateBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as Application).createComponent(this@CreateFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        arguments?.let {
            val subId = it.getString(KEY_SUBSCRIPTION_ID)
            presenter.init(subId)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            SubListContract.REQUEST_KEY, viewLifecycleOwner
        ) { _, result ->
            result.serializable<PredefinedSuggestionItem>(SubListContract.FRAGMENT_RESULT_KEY)?.let {
                presenter.onSuggestionItemClick(it)
            }
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

}