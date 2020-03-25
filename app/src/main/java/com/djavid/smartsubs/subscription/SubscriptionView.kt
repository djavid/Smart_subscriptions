package com.djavid.smartsubs.subscription

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.utils.ACTION_REFRESH_LIST
import com.djavid.smartsubs.utils.animateAlpha
import com.djavid.smartsubs.utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_subscription.view.*

class SubscriptionView(
    private val viewRoot: View
): SubscriptionContract.View {

    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>
    private lateinit var presenter: SubscriptionContract.Presenter

    override fun init(presenter: SubscriptionContract.Presenter) {
        this.presenter = presenter
        setupView()
        setupBottomSheet()
    }

    private fun setupView() {
        viewRoot.sub_closeBtn.setOnClickListener { presenter.onCloseBtnClicked() }
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(viewRoot.sub_bottomSheet)
    }

    override fun expandPanel() {
        viewRoot.post {
            val offset = viewRoot.context.resources.getDimensionPixelOffset(R.dimen.subscription_toolbar_height)
            bottomSheet.setPeekHeight(viewRoot.height - offset, true)
        }
    }

    override fun collapsePanel() {
        viewRoot.post {
            bottomSheet.setPeekHeight(0, true)
        }
    }

    override fun showToolbar(show: Boolean, duration: Long) {
        val fromAlpha = if (show) 0f else 1f
        val toAlpha = if (show) 1f else 0f
        viewRoot.sub_toolbar.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun setBackgroundTransparent(transparent: Boolean, duration: Long) {
        val fromAlpha = if (transparent) 1f else 0f
        val toAlpha = if (transparent) 0f else 1f
        viewRoot.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun hideKeyboard() {
        (viewRoot.context as? AppCompatActivity).hideKeyboard()
    }

    override fun notifyToRefreshSubs() {
        val intent = Intent(ACTION_REFRESH_LIST)
        LocalBroadcastManager.getInstance(viewRoot.context).sendBroadcast(intent)
    }

}