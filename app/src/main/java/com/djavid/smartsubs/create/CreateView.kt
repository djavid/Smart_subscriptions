package com.djavid.smartsubs.create

import android.app.DatePickerDialog
import android.content.Intent
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.getSubPeriodString
import com.djavid.smartsubs.models.getSymbolForCurrency
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.utils.animateAlpha
import com.djavid.smartsubs.utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_create.view.*
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.*

class CreateView(
    private val viewRoot: View
) : CreateContract.View {

    private lateinit var presenter: CreateContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>

    override fun init(presenter: CreateContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()
        setupFormInputs()

        viewRoot.create_closeBtn.setOnClickListener {
            presenter.onCancelPressed()
        }
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        val date = LocalDate(year, month + 1, dayOfMonth)
        presenter.onPaymentDateInputChanged(date)
    }

    private fun setupFormInputs() = with(viewRoot) {
        create_titleInput.doAfterTextChanged {
            presenter.onTitleInputChanged(it?.toString())
        }
        create_priceInput.doAfterTextChanged {
            presenter.onPriceInputChanged(it?.toString()?.toDoubleOrNull())
        }
        create_periodQuantityInput.doAfterTextChanged {
            presenter.onPeriodQuantityInputChanged(it?.toString()?.toIntOrNull())
        }
        create_categoryInput.doAfterTextChanged {
            presenter.onCategoryInputChanged(it?.toString())
        }
        create_noteInput.doAfterTextChanged {
            presenter.onCommentInputChanged(it?.toString())
        }
        create_paymentDateInput.setOnClickListener {
            presenter.onPaymentDateInputPressed()
        }
        create_submitBtn.setOnClickListener {
            presenter.onSubmitPressed()
        }
    }

    override fun openDatePicker(prevSelectedDate: LocalDate?) {
        val dateNow = LocalDate.now(DateTimeZone.forTimeZone(TimeZone.getDefault()))
        val selectedDate = prevSelectedDate ?: dateNow

        DatePickerDialog(
            viewRoot.context, dateSetListener, selectedDate.year,
            selectedDate.monthOfYear - 1, selectedDate.dayOfMonth
        ).show()
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

    override fun getPeriodString(period: SubscriptionPeriodType, quantity: Int): String {
        return viewRoot.context.getSubPeriodString(period, quantity)
    }

    override fun setupSpinner(periods: List<String>) {
        val adapter = ArrayAdapter<String>(viewRoot.context, R.layout.spinner_item, periods)
        viewRoot.create_periodSelector.adapter = adapter
        viewRoot.create_periodSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no-op
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.onPeriodItemSelected(position)
            }
        }
    }

    override fun selectPeriodItem(position: Int) {
        viewRoot.create_periodSelector.setSelection(position, true)
    }

    override fun setCurrencySymbol(currency: Currency) {
        viewRoot.create_currencySymbol.text = viewRoot.context.getSymbolForCurrency(currency)
    }

    override fun setEveryPlural(quantity: Int) {
        val text = viewRoot.context.resources.getQuantityString(R.plurals.plural_every, quantity)
        viewRoot.create_everyTextView.text = text
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

    override fun hideKeyboard() {
        (viewRoot.context as? AppCompatActivity).hideKeyboard()
    }

    override fun notifyToRefresh() {
        val intent = Intent(ACTION_REFRESH)
        LocalBroadcastManager.getInstance(viewRoot.context).sendBroadcast(intent)
    }

    override fun showTitleError(show: Boolean) {
        val drawable = if (show) R.drawable.bg_edittext_error else R.drawable.bg_edittext
        viewRoot.create_titleInput.background = viewRoot.context.getDrawable(drawable)
    }

    override fun showPriceError(show: Boolean) {
        val drawable = if (show) R.drawable.bg_edittext_error else R.drawable.bg_edittext
        viewRoot.create_priceInput.background = viewRoot.context.getDrawable(drawable)
    }

    override fun showQuantityError(show: Boolean) {
        val drawable = if (show) R.drawable.bg_edittext_error else R.drawable.bg_edittext
        viewRoot.create_periodQuantityInput.background = viewRoot.context.getDrawable(drawable)
    }

    override fun setTitle(title: String) {
        viewRoot.create_titleInput.text = SpannableStringBuilder(title)
    }

    override fun setPrice(price: Double) {
        viewRoot.create_priceInput.text = SpannableStringBuilder(price.toString())
    }

    override fun setQuantity(quantity: Int) {
        viewRoot.create_periodQuantityInput.text = SpannableStringBuilder(quantity.toString())
    }

    override fun setDateInput(text: String) {
        viewRoot.create_paymentDateInput.text = SpannableStringBuilder(text)
    }

    override fun setCategory(category: String) {
        viewRoot.create_categoryInput.text = SpannableStringBuilder(category)
    }

    override fun setComment(note: String) {
        viewRoot.create_noteInput.text = SpannableStringBuilder(note)
    }

    override fun switchTitlesToEditMode() {
        viewRoot.create_submitBtnTitle.text = viewRoot.context.getString(R.string.title_save)
        viewRoot.create_title.text = viewRoot.context.getString(R.string.title_save_sub)
    }

}