package com.djavid.smartsubs.subscribe

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_subscribe_media.view.*

class SubscribeMediaView(
    private val viewRoot: View,
    private val fragment: Fragment
) : SubscribeMediaContract.View {

    companion object {
        private const val TG_INTENT_URL = "tg://resolve?domain=smartsubs"
    }

    private lateinit var presenter: SubscribeMediaContract.Presenter

    override fun init(presenter: SubscribeMediaContract.Presenter) {
        this.presenter = presenter

        viewRoot.subMedia_subscribeBtn.setOnClickListener { presenter.onSubscribeClicked() }
        viewRoot.subMedia_noBtn.setOnClickListener { presenter.onNoClicked() }
    }

    override fun close() {
        (fragment as? BottomSheetDialogFragment)?.dismiss()
    }

    override fun openTgChannelInApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TG_INTENT_URL))
            startActivity(viewRoot.context, intent, null)
        } catch (e: ActivityNotFoundException) {
            presenter.tgAppOpenFailed()
        }
    }

    override fun openTgChannelInBrowser(channelUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl))
            startActivity(viewRoot.context, intent, null)
        } catch (e: ActivityNotFoundException) {
            presenter.tgBrowserOpenFailed()
        }
    }

}