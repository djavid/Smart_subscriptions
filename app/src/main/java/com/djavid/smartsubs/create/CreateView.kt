package com.djavid.smartsubs.create

import android.app.DatePickerDialog
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.getSubPeriodString
import com.djavid.smartsubs.models.getSymbolForCurrency
import com.djavid.smartsubs.utils.animateAlpha
import com.djavid.smartsubs.utils.hideKeyboard
import com.djavid.smartsubs.utils.show
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

        viewRoot.create_closeBtn.setOnClickListener { presenter.onCancelPressed() }
        viewRoot.create_logoBtn.setOnClickListener { presenter.onPredefinedBtnPressed() }
        viewRoot.create_predefinedBtn.setOnClickListener { presenter.onPredefinedBtnPressed() }
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
        create_trialPeriodCheckbox.setOnCheckedChangeListener { _, isChecked ->
            presenter.onTrialPeriodChecked(isChecked)
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

    override fun setupSuggestions(items: List<PredefinedSuggestionItem>) {
        val adapter = SuggestionsAdapter(items, viewRoot.context)
        viewRoot.create_titleInput.setAdapter(adapter)
        viewRoot.create_titleInput.setOnItemClickListener { _, _, position, _ ->
            presenter.onSuggestionItemClick(items[position])
        }
    }

    override fun setSubLogo(bytes: ByteArray?) {
        if (bytes == null) {
            viewRoot.create_predefinedBtn.show(false) //todo should be true on next release
            viewRoot.create_logoBtn.show(false)
        } else {
            viewRoot.create_predefinedBtn.show(false)
            viewRoot.create_logoBtn.show(true)
            Glide.with(viewRoot.context)
                .load(bytes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewRoot.create_logoBtn)
        }
    }

    override fun enableInputs(enable: Boolean) {
        viewRoot.create_titleInput.isEnabled = enable
        viewRoot.create_priceInput.isEnabled = enable
        viewRoot.create_periodQuantityInput.isEnabled = enable
        viewRoot.create_trialPeriodCheckbox.isEnabled = enable
        viewRoot.create_paymentDateInput.isEnabled = enable
        viewRoot.create_periodSelector.isEnabled = enable
        viewRoot.create_categoryInput.isEnabled = enable
        viewRoot.create_noteInput.isEnabled = enable
        viewRoot.create_submitBtn.isEnabled = enable
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
        val adapter = ArrayAdapter(viewRoot.context, R.layout.spinner_item, periods)
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

    override fun setPaymentDateTrialDescription() {
        viewRoot.create_firstPayDescription.text = viewRoot.context.getString(R.string.desc_trial_pay)
    }

    override fun setPaymentDateDefaultDescription() {
        viewRoot.create_firstPayDescription.text = viewRoot.context.getString(R.string.desc_first_pay)
    }

    override fun setTrialPeriodCheckbox(checked: Boolean) {
        viewRoot.create_trialPeriodCheckbox.isChecked = checked
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

    override fun showTitleError(show: Boolean) {
        viewRoot.create_titleInput.setError(show)
    }

    override fun showPriceError(show: Boolean) {
        val drawable = if (show) R.drawable.bg_edittext_error else R.drawable.bg_edittext
        viewRoot.create_priceInput.background = ContextCompat.getDrawable(viewRoot.context, drawable)
    }

    override fun showQuantityError(show: Boolean) {
        viewRoot.create_periodQuantityInput.setError(show)
    }

    override fun showPaymentDateError(show: Boolean) {
        viewRoot.create_paymentDateInput.setError(show)
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
        viewRoot.create_submitBtn.text = viewRoot.context.getString(R.string.title_save)
        viewRoot.create_title.text = viewRoot.context.getString(R.string.title_edit)
    }

}