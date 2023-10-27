package com.djavid.smartsubs.subscribe_media

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.djavid.smartsubs.databinding.DialogSubscribeMediaBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SubscribeMediaView(
    private val fragment: Fragment,
    private val binding: DialogSubscribeMediaBinding
) : SubscribeMediaContract.View {

    companion object {
        private const val TG_INTENT_URL = "tg://resolve?domain=smartsubs"
    }

    private lateinit var presenter: SubscribeMediaContract.Presenter
    private val context = binding.root.context

    override fun init(presenter: SubscribeMediaContract.Presenter) {
        this.presenter = presenter

        binding.subMediaSubscribeBtn.setOnClickListener { presenter.onSubscribeClicked() }
        binding.subMediaNoBtn.setOnClickListener { presenter.onNoClicked() }
    }

    override fun close() {
        (fragment as? BottomSheetDialogFragment)?.dismiss()
    }

    override fun openTgChannelInApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TG_INTENT_URL))
            startActivity(context, intent, null)
        } catch (e: ActivityNotFoundException) {
            presenter.tgAppOpenFailed()
        }
    }

    override fun openTgChannelInBrowser(channelUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl))
            startActivity(context, intent, null)
        } catch (e: ActivityNotFoundException) {
            presenter.tgBrowserOpenFailed()
        }
    }

}