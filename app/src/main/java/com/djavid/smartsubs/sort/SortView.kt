package com.djavid.smartsubs.sort

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.databinding.FragmentSortBinding
import com.djavid.smartsubs.models.SortBy
import com.djavid.smartsubs.utils.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortView(
    private val fragment: Fragment,
    private val binding: FragmentSortBinding
) : SortContract.View {

    private val context = binding.root.context
    private lateinit var presenter: SortContract.Presenter

    override fun init(presenter: SortContract.Presenter) {
        this.presenter = presenter
    }

    override fun showSortByList(items: List<SortBy>) {
        val adapter = SortByAdapter(presenter::onSortItemClicked)
        binding.sortByRecycler.adapter = adapter

        getDrawable(context, R.drawable.bg_recycler_divider)?.let {
            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(it)
            binding.sortByRecycler.addItemDecoration(itemDecoration)
        }

        binding.sortByRecycler.show(true)
        adapter.showData(items)
    }

    override fun showSortTitle(show: Boolean) {
        binding.sortTitle.show(show)
    }

    override fun setSortBySelection(sortBy: SortBy) {
        val title = sortBy.getTitle(context)
        binding.sortByTitle.text = title
        binding.sortByBtn.show(true)
        binding.sortByBtn.setOnClickListener { presenter.onSortByClicked() }
    }

    override fun showSortDelimiter(show: Boolean) {
        binding.sortByDelimiter.show(show)
    }

    override fun setActiveSortType(asc: Boolean) {
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

        binding.sortByAscSelector.show(true)
        binding.sortByDescSelector.show(true)

        binding.sortByAscSelector.setOnClickListener { presenter.onAscSortClicked() }
        binding.sortByDescSelector.setOnClickListener { presenter.onDescSortClicked() }
    }

    override fun finish() {
        (fragment as? BottomSheetDialogFragment)?.dismiss()
    }

}