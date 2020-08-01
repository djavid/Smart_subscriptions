package com.djavid.smartsubs.sort

import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.SortBy
import com.djavid.smartsubs.utils.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_sort.view.*


class SortView(
    private val viewRoot: View,
    private val fragment: Fragment
) : SortContract.View {

    private lateinit var presenter: SortContract.Presenter

    override fun init(presenter: SortContract.Presenter) {
        this.presenter = presenter
    }

    override fun showSortByList(items: List<SortBy>) {
        val adapter = SortByAdapter(presenter::onSortItemClicked)
        viewRoot.sortBy_recycler.layoutManager = LinearLayoutManager(viewRoot.context)
        viewRoot.sortBy_recycler.adapter = adapter

        getDrawable(viewRoot.context, R.drawable.bg_recycler_divider)?.let {
            val itemDecoration = DividerItemDecoration(viewRoot.context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(it)
            viewRoot.sortBy_recycler.addItemDecoration(itemDecoration)
        }

        viewRoot.sortBy_recycler.show(true)
        adapter.showData(items)
    }

    override fun showSortTitle(show: Boolean) {
        viewRoot.sort_title.show(show)
    }

    override fun setSortBySelection(sortBy: SortBy) {
        val title = sortBy.getTitle(viewRoot.context)
        viewRoot.sortBy_title.text = title
        viewRoot.sortBy_Btn.show(true)
        viewRoot.sortBy_Btn.setOnClickListener { presenter.onSortByClicked() }
    }

    override fun showSortDelimiter(show: Boolean) {
        viewRoot.sortBy_delimiter.show(show)
    }

    override fun setActiveSortType(asc: Boolean) {
        if (asc) {
            TextViewCompat.setTextAppearance(viewRoot.sortBy_ascSelector, R.style.ButtonStyleActive)
            TextViewCompat.setTextAppearance(viewRoot.sortBy_descSelector, R.style.ButtonStyle)

            viewRoot.sortBy_ascSelector.setBackgroundResource(R.drawable.bg_button_active)
            viewRoot.sortBy_descSelector.setBackgroundResource(R.drawable.bg_button)
        } else {
            TextViewCompat.setTextAppearance(viewRoot.sortBy_ascSelector, R.style.ButtonStyle)
            TextViewCompat.setTextAppearance(viewRoot.sortBy_descSelector, R.style.ButtonStyleActive)

            viewRoot.sortBy_ascSelector.setBackgroundResource(R.drawable.bg_button)
            viewRoot.sortBy_descSelector.setBackgroundResource(R.drawable.bg_button_active)
        }

        viewRoot.sortBy_ascSelector.show(true)
        viewRoot.sortBy_descSelector.show(true)

        viewRoot.sortBy_ascSelector.setOnClickListener { presenter.onAscSortClicked() }
        viewRoot.sortBy_descSelector.setOnClickListener { presenter.onDescSortClicked() }
    }

    override fun finish() {
        (fragment as? BottomSheetDialogFragment)?.dismiss()
    }

}