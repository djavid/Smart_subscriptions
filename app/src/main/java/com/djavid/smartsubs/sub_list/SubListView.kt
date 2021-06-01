package com.djavid.smartsubs.sub_list

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.smartsubs.utils.animateAlpha
import com.djavid.smartsubs.utils.hideKeyboard
import com.djavid.smartsubs.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_sub_list.view.*

class SubListView(
    private val viewRoot: View
) : SubListContract.View {

    private lateinit var presenter: SubListContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>

    override fun init(presenter: SubListContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()
        viewRoot.subList_recycler.adapter = SubsListAdapter(presenter::onItemClick)

        viewRoot.subList_closeBtn.setOnClickListener { presenter.onBackPressed() }
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(viewRoot.subList_bottomSheet)
    }

    override fun showToolbar(show: Boolean, duration: Long) {
        val fromAlpha = if (show) 0f else 1f
        val toAlpha = if (show) 1f else 0f
        viewRoot.subList_toolbar.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun setBackgroundTransparent(transparent: Boolean, duration: Long) {
        val fromAlpha = if (transparent) 1f else 0f
        val toAlpha = if (transparent) 0f else 1f
        viewRoot.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun hideKeyboard() {
        (viewRoot.context as? AppCompatActivity).hideKeyboard()
    }

    override fun collapsePanel() {
        viewRoot.post {
            bottomSheet.setPeekHeight(0, true)
        }
    }

    override fun expandPanel() {
        viewRoot.post {
            val offset = viewRoot.context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(viewRoot.height - offset, true)
        }
    }

    override fun showPredefinedSubs(list: List<PredefinedSuggestionItem>) {
        (viewRoot.subList_recycler.adapter as? SubsListAdapter)?.showSubs(list)
    }

    override fun showProgress(show: Boolean) {
        viewRoot.subList_progress.show(show)
    }

}