package com.djavid.smartsubs.currency_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.djavid.features.currency_list.databinding.FragmentCurrencyListBinding
import com.djavid.smartsubs.common.base.BackPressListener
import com.djavid.smartsubs.common.base.BaseFragment
import com.djavid.smartsubs.common.SmartSubsApplication
import org.kodein.di.instance

class CurrencyListFragment : BaseFragment(), BackPressListener {

    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val presenter: CurrencyListContract.Presenter by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCurrencyListBinding.inflate(inflater).apply {
            _binding = this
            di = (requireActivity().application as SmartSubsApplication)
                .currencyListComponent(this@CurrencyListFragment, binding)
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