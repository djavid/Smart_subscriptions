package com.djavid.smartsubs.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.smartsubs.Application
import com.djavid.common.BaseBottomSheetFragment
import com.djavid.smartsubs.databinding.FragmentSortBinding
import com.djavid.common.KEY_SORT_SCREEN_TYPE
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import org.kodein.di.DIAware
import org.kodein.di.instance

class SortFragment : com.djavid.common.BaseBottomSheetFragment(), DIAware {

    private val presenter: SortContract.Presenter by instance()
    private lateinit var binding: FragmentSortBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSortBinding.inflate(inflater, container, false).apply {
            binding = this
            di = (requireActivity().application as Application).sortComponent(this@SortFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getScreenType {
            presenter.init(it)
        }

        BottomSheetBehavior.from(binding.sortBottomSheet).apply { this.state = STATE_EXPANDED }
    }

    private fun getScreenType(action: (SortContract.ScreenType) -> Unit) {
        requireArguments().getString(com.djavid.common.KEY_SORT_SCREEN_TYPE)?.let {
            action.invoke(SortContract.ScreenType.valueOf(it))
        }
    }

}