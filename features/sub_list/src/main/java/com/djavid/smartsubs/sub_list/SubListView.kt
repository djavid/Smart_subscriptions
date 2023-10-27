package com.djavid.smartsubs.sub_list

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.djavid.smartsubs.R
import com.djavid.smartsubs.databinding.FragmentSubListBinding
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.common.animateAlpha
import com.djavid.common.hideKeyboard
import com.djavid.common.show
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SubListView(
    private val binding: FragmentSubListBinding
) : SubListContract.View {

    private lateinit var presenter: SubListContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>
    private val context = binding.root.context

    override fun init(presenter: SubListContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()
        binding.subListRecycler.adapter = SubsListAdapter(presenter::onItemClick)

        binding.subListCloseBtn.setOnClickListener { presenter.onBackPressed() }
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(binding.subListBottomSheet)
    }

    override fun showToolbar(show: Boolean, duration: Long) {
        val fromAlpha = if (show) 0f else 1f
        val toAlpha = if (show) 1f else 0f
        binding.subListToolbar.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun setBackgroundTransparent(transparent: Boolean, duration: Long) {
        val fromAlpha = if (transparent) 1f else 0f
        val toAlpha = if (transparent) 0f else 1f
        binding.root.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun hideKeyboard() {
        (context as? AppCompatActivity).hideKeyboard()
    }

    override fun collapsePanel() {
        binding.root.post {
            bottomSheet.setPeekHeight(0, true)
        }
    }

    override fun expandPanel() {
        binding.root.post {
            val offset = context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(binding.root.height - offset, true)
        }
    }

    override fun showPredefinedSubs(list: List<PredefinedSuggestionItem>) {
        (binding.subListRecycler.adapter as? SubsListAdapter)?.showSubs(list)
    }

    override fun showProgress(show: Boolean) {
        binding.subListProgress.show(show)
    }

}