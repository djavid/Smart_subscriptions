package com.djavid.smartsubs.subscription

import android.app.AlertDialog
import android.content.DialogInterface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.djavid.smartsubs.R
import com.djavid.smartsubs.databinding.FragmentSubscriptionBinding
import com.djavid.smartsubs.models.*
import com.djavid.smartsubs.utils.DECIMAL_FORMAT
import com.djavid.smartsubs.utils.animateAlpha
import com.djavid.smartsubs.utils.hideKeyboard
import com.djavid.smartsubs.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.DecimalFormat

class SubscriptionView(
    private val binding: FragmentSubscriptionBinding
) : SubscriptionContract.View {

    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>
    private lateinit var presenter: SubscriptionContract.Presenter

    override fun init(presenter: SubscriptionContract.Presenter) {
        this.presenter = presenter
        setupView()
        setupBottomSheet()
    }

    private fun setupView() {
        binding.subCloseBtn.setOnClickListener { presenter.onCloseBtnClicked() }
        binding.subEditBtn.setOnClickListener { presenter.onEditClicked() }
        binding.subDeleteBtn.setOnClickListener { presenter.onDeleteClicked() }
        binding.subNotifsBtn.setOnClickListener { presenter.onNotifsClicked() }
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(binding.subBottomSheet)
    }

    override fun setSubLogo(bytes: ByteArray?) {
        if (bytes != null) {
            Glide.with(binding.root.context)
                .load(bytes)
                .into(binding.subLogo)
        }
        binding.subLogo.show(bytes != null)
    }

    override fun showDeletionPromptDialog() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    presenter.onDeletionPrompted()
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.dismiss()
                }
            }
        }

        val builder = AlertDialog.Builder(binding.root.context)
        builder.setMessage(binding.root.context.getString(R.string.title_are_you_sure))
            .setPositiveButton(binding.root.context.getString(R.string.title_yes), dialogClickListener)
            .setNegativeButton(binding.root.context.getString(R.string.title_no), dialogClickListener)
            .show()
    }

    override fun expandPanel(biggerToolbar: Boolean) {
        binding.root.post {
            val offset = binding.root.context.resources.getDimensionPixelOffset(
                if (biggerToolbar)
                    R.dimen.subscription_toolbar_height
                else
                    R.dimen.subscription_toolbar_without_category_height
            )

            bottomSheet.setPeekHeight(binding.root.height - offset, true)
        }
    }

    override fun collapsePanel() {
        binding.root.post {
            bottomSheet.setPeekHeight(0, true)
        }
    }

    override fun showToolbar(show: Boolean, duration: Long) {
        val fromAlpha = if (show) 0f else 1f
        val toAlpha = if (show) 1f else 0f
        binding.subToolbar.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun setBackgroundTransparent(transparent: Boolean, duration: Long) {
        val fromAlpha = if (transparent) 1f else 0f
        val toAlpha = if (transparent) 0f else 1f
        binding.root.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun hideKeyboard() {
        (binding.root.context as? AppCompatActivity).hideKeyboard()
    }

    override fun setTitle(title: String) {
        binding.subTitle.text = title
    }

    override fun setCategory(category: String) {
        binding.subCategory.show(true)
        binding.subCategory.text = category
    }

    override fun setPrice(period: SubscriptionPeriod, price: SubscriptionPrice) = with(binding) {
        val currSymbol = price.currency.symbol
        val everyPlural = binding.root.context.resources.getQuantityString(R.plurals.plural_every, period.quantity)

        val periodPlural = binding.root.context.getSubPeriodString(period.type, period.quantity)
        val priceFormatted = DecimalFormat(DECIMAL_FORMAT).format(price.value)


        val text = if (period.quantity == 1) {
            if (period.type == SubscriptionPeriodType.WEEK) {
                binding.root.context.getString(
                    R.string.mask_subscription_price_3_words, priceFormatted,
                    currSymbol, binding.root.context.getString(R.string.every_week)
                )
            } else {
                binding.root.context.getString(
                    R.string.mask_subscription_price, priceFormatted,
                    currSymbol, everyPlural, periodPlural
                )
            }
        } else {
            binding.root.context.getString(
                R.string.mask_subscription_price_quantity, priceFormatted,
                currSymbol, everyPlural, period.quantity, periodPlural
            )
        }
        subPrice.text = text.toPriceSpannable()
    }

    override fun setComment(comment: String) {
        binding.subComment.show(true)
        binding.subComment.text = comment
    }

    override fun setNextPayment(progress: SubscriptionProgress) = with(binding) {
        val text = if (progress.daysLeft == 0) {
            binding.root.context.getString(R.string.title_next_payment_today)
        } else {
            val periodPlural = binding.root.context.getSubPeriodString(SubscriptionPeriodType.DAY, progress.daysLeft)
            binding.root.context.getString(R.string.mask_next_payment, progress.daysLeft, periodPlural)
        }

        subNextPayment.text = text.toPinkSpannable()
        subProgressBar.progress = (progress.value * 100).toInt()
        subNextPayment.show(true)
        subProgressBar.show(true)
        subNextPaymentDivider.show(true)
    }

    override fun setOverallSpent(spent: SubscriptionPrice) = with(binding) {
        val currSymbol = spent.currency.symbol
        val spentFormatted = DecimalFormat(DECIMAL_FORMAT).format(spent.value)
        val text = binding.root.context.getString(R.string.mask_overall_spent, spentFormatted, currSymbol)

        subOverallSpent.text = text.toPinkSpannable()
        subOverallSpent.show(true)
        subSpentDivider.show(true)
    }

    private fun String.toPriceSpannable(): SpannableString {
        val spannable = SpannableString(this)

        val firstWhitespace = spannable.indexOf(' ')
        val secondWhitespace = spannable.indexOf(' ', firstWhitespace + 1)
        val spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        val colorGray = ContextCompat.getColor(binding.root.context, R.color.colorGray)

        spannable.setSpan(AbsoluteSizeSpan(17, true), secondWhitespace + 1, length, spanFlag)
        spannable.setSpan(ForegroundColorSpan(colorGray), secondWhitespace + 1, length, spanFlag)

        return spannable
    }

    private fun String.toPinkSpannable(): SpannableString {
        val spannable = SpannableString(this)

        val firstWhitespace = spannable.indexOf(' ')
        val secondWhitespace = spannable.indexOf(' ', firstWhitespace + 1)
        val spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        val colorPink = ContextCompat.getColor(binding.root.context, R.color.colorPinkishOrange)

        spannable.setSpan(ForegroundColorSpan(colorPink), secondWhitespace + 1, length, spanFlag)

        return spannable
    }

    override fun setNotifsCount(notifs: Int) {
        val plural = binding.root.context.resources.getQuantityString(R.plurals.plural_notification, notifs)
        binding.subNotifsBtnTitle.text = binding.root.context.getString(R.string.mask_notifs_count, notifs, plural)
    }

    override fun showNotifsSection(show: Boolean) {
        binding.subNotifsBtn.show(show)
    }

}