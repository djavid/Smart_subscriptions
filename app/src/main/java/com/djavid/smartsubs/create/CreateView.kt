package com.djavid.smartsubs.create

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.djavid.smartsubs.R
import com.djavid.smartsubs.utils.animateAlpha
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_create.view.*


class CreateView(
    private val viewRoot: View
) : CreateContract.View {

    companion object {
        const val SLIDE_DURATION = 256L // ms
    }

    private lateinit var presenter: CreateContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            //no-op
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                presenter.onPanelExpanded()
            }
        }
    }

    override fun init(presenter: CreateContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()

        viewRoot.create_closeBtn.setOnClickListener {
            presenter.onCancelPressed()
        }
    }

    private fun setupBottomSheet() {
        val offset = viewRoot.context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
        bottomSheet = BottomSheetBehavior.from(viewRoot.create_bottomSheet)
        bottomSheet.addBottomSheetCallback(bottomSheetCallback)
        bottomSheet.isFitToContents = false
        bottomSheet.setExpandedOffset(offset)
    }

    override fun expandPanel() {
        viewRoot.post {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun collapsePanel() {
        viewRoot.post {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun showToolbar(show: Boolean, duration: Long) {
        val fromAlpha = if (show) 0f else 1f
        val toAlpha = if (show) 1f else 0f
        viewRoot.create_toolbar.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun setBackgroundTransparent(transparent: Boolean, duration: Long) {
        val fromAlpha = if (transparent) 1f else 0f
        val toAlpha = if (transparent) 0f else 1f
        viewRoot.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun goBack() {
        (viewRoot.context as? AppCompatActivity)?.onBackPressed()
    }

}