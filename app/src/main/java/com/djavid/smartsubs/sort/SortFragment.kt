package com.djavid.smartsubs.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BaseBottomSheetFragment
import com.djavid.smartsubs.databinding.FragmentSortBinding
import com.djavid.smartsubs.utils.KEY_SORT_SCREEN_TYPE
import com.djavid.smartsubs.utils.setWhiteNavigationBar
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class SortFragment : BaseBottomSheetFragment(R.layout.fragment_sort), DIAware {

    private val presenter: SortContract.Presenter by instance()
    private lateinit var binding: FragmentSortBinding
    override lateinit var di: DI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSortBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as Application).sortComponent(this@SortFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getScreenType {
            presenter.init(it)
            dialog?.window?.setWhiteNavigationBar()
        }
    }

    private fun getScreenType(action: (SortContract.ScreenType) -> Unit) {
        requireArguments().getString(KEY_SORT_SCREEN_TYPE)?.let {
            action.invoke(SortContract.ScreenType.valueOf(it))
        }
    }

}