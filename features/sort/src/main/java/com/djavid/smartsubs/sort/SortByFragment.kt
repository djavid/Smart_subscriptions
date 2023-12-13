package com.djavid.smartsubs.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import com.djavid.core.ui.R
import com.djavid.features.sort.databinding.FragmentSortByBinding
import com.djavid.smartsubs.common.base.BaseBottomSheetFragment
import com.djavid.smartsubs.common.models.SortBy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import org.kodein.di.DIAware
import org.kodein.di.instance

class SortByFragment : BaseBottomSheetFragment(), DIAware {

    private lateinit var binding: FragmentSortByBinding

    private val viewModel: SortViewModel by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentSortByBinding.inflate(inflater, container, false).apply {
            binding = this
            di = (requireParentFragment() as DIAware).di
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomSheetBehavior.from(binding.sortByBottomSheet).apply { this.state = STATE_EXPANDED }

        viewModel.sortByValues.observe(viewLifecycleOwner) { showSortByList(it) }
        requireParentFragment()
    }

    private fun showSortByList(items: List<SortBy>) {
        val adapter = SortByAdapter(
            data = items,
            onClick = viewModel::selectSortByItem
        )
        binding.sortByRecycler.adapter = adapter

        AppCompatResources.getDrawable(requireContext(), R.drawable.bg_recycler_divider)?.let {
            binding.sortByRecycler.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply { setDrawable(it) }
            )
        }
    }
}