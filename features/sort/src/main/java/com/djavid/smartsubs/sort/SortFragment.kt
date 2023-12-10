package com.djavid.smartsubs.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.features.sort.databinding.FragmentSortBinding
import com.djavid.smartsubs.common.base.BaseBottomSheetFragment
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.utils.Constants.KEY_SORT_SCREEN_TYPE
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import org.kodein.di.DIAware
import org.kodein.di.instance

class SortFragment : BaseBottomSheetFragment(), DIAware {

    private val presenter: SortContract.Presenter by instance()
    private lateinit var binding: FragmentSortBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSortBinding.inflate(inflater, container, false).apply {
            binding = this
            di = (requireActivity().application as SmartSubsApplication).sortComponent(this@SortFragment, binding)
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
        requireArguments().getString(KEY_SORT_SCREEN_TYPE)?.let {
            action.invoke(SortContract.ScreenType.valueOf(it))
        }
    }
}