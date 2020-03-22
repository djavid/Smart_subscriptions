package com.djavid.smartsubs.create

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.getSubPeriodString
import com.djavid.smartsubs.utils.animateAlpha
import com.djavid.smartsubs.utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_create.view.*


class CreateView(
    private val viewRoot: View
) : CreateContract.View {

    companion object {
        const val SLIDE_DURATION = 256L // ms
    }

    private lateinit var presenter: CreateContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>

    override fun init(presenter: CreateContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()

        viewRoot.create_closeBtn.setOnClickListener {
            presenter.onCancelPressed()
        }
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(viewRoot.create_bottomSheet)
    }

    override fun expandPanel() {
        viewRoot.post {
            val offset = viewRoot.context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(viewRoot.height - offset, true)
        }
    }

    override fun collapsePanel() {
        viewRoot.post {
            bottomSheet.setPeekHeight(0, true)
        }
    }

    override fun getPeriodString(period: SubscriptionPeriodType): String {
        return viewRoot.context.getSubPeriodString(period)
    }

    override fun setupSpinner(periods: List<String>) {
        val adapter = ArrayAdapter<String>(viewRoot.context, R.layout.spinner_item, periods)
        viewRoot.create_periodSelector.adapter = adapter
        viewRoot.create_periodSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no-op
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.onItemSelected(position)
            }
        }
    }

    override fun selectPeriodItem(position: Int) {
        viewRoot.create_periodSelector.setSelection(position, true)
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
        (viewRoot.context as? AppCompatActivity)?.supportFragmentManager?.popBackStack()
    }

    override fun hideKeyboard() {
        (viewRoot.context as? AppCompatActivity).hideKeyboard()
    }

}