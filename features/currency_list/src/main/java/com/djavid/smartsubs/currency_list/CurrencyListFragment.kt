package com.djavid.smartsubs.currency_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.djavid.smartsubs.Application
import com.djavid.common.BackPressListener
import com.djavid.common.BaseFragment
import com.djavid.smartsubs.currency_list.CurrencyListContract
import com.djavid.smartsubs.databinding.FragmentCurrencyListBinding
import org.kodein.di.instance

class CurrencyListFragment : com.djavid.common.BaseFragment(), com.djavid.common.BackPressListener {

    private lateinit var binding: FragmentCurrencyListBinding
    private val presenter: CurrencyListContract.Presenter by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCurrencyListBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as Application)
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