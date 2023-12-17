package com.djavid.smartsubs.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import com.djavid.core.ui.R
import com.djavid.features.sort.databinding.FragmentSortBinding
import com.djavid.smartsubs.common.base.BaseBottomSheetFragment
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.domain.SortBy
import com.djavid.smartsubs.common.domain.SortType
import com.djavid.smartsubs.common.utils.show
import com.djavid.ui.getTitle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import org.kodein.di.instance

class SortFragment : BaseBottomSheetFragment() {

    private val viewModel: SortViewModel by instance()

    private lateinit var binding: FragmentSortBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentSortBinding.inflate(inflater, container, false).apply {
            binding = this
            di = (requireActivity().application as SmartSubsApplication).sortComponent(this@SortFragment)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomSheetBehavior.from(binding.sortBottomSheet).apply { this.state = STATE_EXPANDED }

        viewModel.selectedSortBy.observe(viewLifecycleOwner) { setSortBySelection(it) }
        viewModel.selectedSortType.observe(viewLifecycleOwner) { setActiveSortType(it == SortType.ASC) }
    }

    private fun setSortBySelection(sortBy: SortBy) {
        val title = sortBy.getTitle(requireContext())
        binding.sortByTitle.text = title
        binding.sortByBtn.show(true)
        binding.sortByBtn.setOnClickListener { viewModel.openSortByScreen() }
    }

    private fun setActiveSortType(asc: Boolean) {
        if (asc) {
            TextViewCompat.setTextAppearance(binding.sortByAscSelector, R.style.ButtonStyleActive)
            TextViewCompat.setTextAppearance(binding.sortByDescSelector, R.style.ButtonStyle)

            binding.sortByAscSelector.setBackgroundResource(R.drawable.bg_button_active)
            binding.sortByDescSelector.setBackgroundResource(R.drawable.bg_button)
        } else {
            TextViewCompat.setTextAppearance(binding.sortByAscSelector, R.style.ButtonStyle)
            TextViewCompat.setTextAppearance(binding.sortByDescSelector, R.style.ButtonStyleActive)

            binding.sortByAscSelector.setBackgroundResource(R.drawable.bg_button)
            binding.sortByDescSelector.setBackgroundResource(R.drawable.bg_button_active)
        }

        binding.sortByAscSelector.setOnClickListener { viewModel.selectSortType(SortType.ASC) }
        binding.sortByDescSelector.setOnClickListener { viewModel.selectSortType(SortType.DESC) }
    }

}