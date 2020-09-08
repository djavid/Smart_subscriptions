package com.djavid.smartsubs.subscription

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.*
import com.djavid.smartsubs.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_subscription.view.*
import java.text.DecimalFormat

class SubscriptionView(
    private val viewRoot: View
) : SubscriptionContract.View {

    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>
    private lateinit var presenter: SubscriptionContract.Presenter

    override fun init(presenter: SubscriptionContract.Presenter) {
        this.presenter = presenter
        setupView()
        setupBottomSheet()
    }

    private fun setupView() {
        viewRoot.sub_closeBtn.setOnClickListener { presenter.onCloseBtnClicked() }
        viewRoot.sub_editBtn.setOnClickListener { presenter.onEditClicked() }
        viewRoot.sub_deleteBtn.setOnClickListener { presenter.onDeleteClicked() }
        viewRoot.sub_notifsBtn.setOnClickListener { presenter.onNotifsClicked() }
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(viewRoot.sub_bottomSheet)
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

        val builder = AlertDialog.Builder(viewRoot.context)
        builder.setMessage(viewRoot.context.getString(R.string.title_are_you_sure))
            .setPositiveButton(viewRoot.context.getString(R.string.title_yes), dialogClickListener)
            .setNegativeButton(viewRoot.context.getString(R.string.title_no), dialogClickListener)
            .show()
    }

    override fun expandPanel(biggerToolbar: Boolean) {
        viewRoot.post {
            val offset = viewRoot.context.resources.getDimensionPixelOffset(
                if (biggerToolbar)
                    R.dimen.subscription_toolbar_height
                else
                    R.dimen.subscription_toolbar_without_category_height
            )

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

    override fun notifyToRefresh() {
        val intent = Intent(ACTION_REFRESH)
        LocalBroadcastManager.getInstance(viewRoot.context).sendBroadcast(intent)
    }

    override fun setTitle(title: String) {
        viewRoot.sub_title.text = title
    }

    override fun setCategory(category: String) {
        viewRoot.sub_category.show(true)
        viewRoot.sub_category.text = category
    }

    override fun setPrice(period: SubscriptionPeriod, price: SubscriptionPrice) = with(viewRoot) {
        val currSymbol = context.getSymbolForCurrency(price.currency)
        val everyPlural = viewRoot.context.resources.getQuantityString(R.plurals.plural_every, period.quantity)
        val periodPlural = context.getSubPeriodString(period.type, period.quantity)
        val priceFormatted = DecimalFormat(DECIMAL_FORMAT).format(price.value)

        val text = if (period.quantity == 1) {
            context.getString(
                R.string.mask_subscription_price, priceFormatted,
                currSymbol, everyPlural, periodPlural
            )

        } else {
            context.getString(
                R.string.mask_subscription_price_quantity, priceFormatted,
                currSymbol, everyPlural, period.quantity, periodPlural
            )
        }
        sub_price.text = text.toPriceSpannable()
    }

    override fun setComment(comment: String) {
        viewRoot.sub_comment.show(true)
        viewRoot.sub_comment.text = comment
    }

    override fun setNextPayment(progress: SubscriptionProgress) = with(viewRoot) {
        val text = if (progress.daysLeft == 0) {
            context.getString(R.string.title_next_payment_today)
        } else {
            val periodPlural = context.getSubPeriodString(SubscriptionPeriodType.DAY, progress.daysLeft)
            context.getString(R.string.mask_next_payment, progress.daysLeft, periodPlural)
        }

        sub_nextPayment.text = text.toPinkSpannable()
        sub_progressBar.progress = (progress.progress * 100).toInt()
        sub_nextPayment.show(true)
        sub_progressBar.show(true)
        sub_nextPaymentDivider.show(true)
    }

    override fun setOverallSpent(spent: SubscriptionPrice) = with(viewRoot) {
        val currSymbol = context.getSymbolForCurrency(spent.currency)
        val spentFormatted = DecimalFormat(DECIMAL_FORMAT).format(spent.value)
        val text = context.getString(R.string.mask_overall_spent, spentFormatted, currSymbol)

        sub_overallSpent.text = text.toPinkSpannable()
        sub_overallSpent.show(true)
        sub_spentDivider.show(true)
    }

    private fun String.toPriceSpannable(): SpannableString {
        val spannable = SpannableString(this)

        val firstWhitespace = spannable.indexOf(' ')
        val secondWhitespace = spannable.indexOf(' ', firstWhitespace + 1)
        val spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        val colorGray = ContextCompat.getColor(viewRoot.context, R.color.colorGray)

        spannable.setSpan(AbsoluteSizeSpan(17, true), secondWhitespace + 1, length, spanFlag)
        spannable.setSpan(ForegroundColorSpan(colorGray), secondWhitespace + 1, length, spanFlag)

        return spannable
    }

    private fun String.toPinkSpannable(): SpannableString {
        val spannable = SpannableString(this)

        val firstWhitespace = spannable.indexOf(' ')
        val secondWhitespace = spannable.indexOf(' ', firstWhitespace + 1)
        val spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        val colorPink = ContextCompat.getColor(viewRoot.context, R.color.colorPinkishOrange)

        spannable.setSpan(ForegroundColorSpan(colorPink), secondWhitespace + 1, length, spanFlag)

        return spannable
    }

    override fun setNotifsCount(notifs: Int) {
        val plural = viewRoot.context.resources.getQuantityString(R.plurals.plural_notification, notifs)
        viewRoot.sub_notifsBtn_title.text = viewRoot.context.getString(R.string.mask_notifs_count, notifs, plural)
    }

    override fun showNotifsSection(show: Boolean) {
        viewRoot.sub_notifsBtn.show(show)
    }

}