package com.djavid.smartsubs.currency_list

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.djavid.core.ui.R
import com.djavid.features.currency_list.databinding.FragmentCurrencyListBinding
import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import com.djavid.smartsubs.common.utils.animateAlpha
import com.djavid.smartsubs.common.utils.hideKeyboard
import com.djavid.smartsubs.common.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CurrencyListView(
    private val binding: FragmentCurrencyListBinding
) : CurrencyListContract.View {

    private lateinit var presenter: CurrencyListContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>
    private val context = binding.root.context

    override fun init(presenter: CurrencyListContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()
        binding.currencyListRecycler.adapter = CurrencyListAdapter(presenter::onItemClick)
        binding.currencyListCloseBtn.setOnClickListener { presenter.onBackPressed() }
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(binding.currencyListBottomSheet)
    }

    override fun showToolbar(show: Boolean, duration: Long) {
        val fromAlpha = if (show) 0f else 1f
        val toAlpha = if (show) 1f else 0f
        binding.currencyListToolbar.animateAlpha(fromAlpha, toAlpha, duration)
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
        (binding.currencyListRecycler.adapter as? CurrencyListAdapter)?.showSubs(list)
    }

    override fun showProgress(show: Boolean) {
        binding.currencyListProgress.show(show)
    }

}