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
import com.djavid.core.ui.R
import com.djavid.features.create.databinding.FragmentCreateBinding
import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import com.djavid.smartsubs.common.models.SubscriptionPeriodType
import com.djavid.smartsubs.common.utils.animateAlpha
import com.djavid.smartsubs.common.utils.getCurrencySymbol
import com.djavid.smartsubs.common.utils.hideKeyboard
import com.djavid.smartsubs.common.utils.show
import com.djavid.ui.getSubPeriodString
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.*

class CreateView(
    private val binding: FragmentCreateBinding
) : CreateContract.View {

    private lateinit var presenter: CreateContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<FrameLayout>

    override fun init(presenter: CreateContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()
        setupFormInputs()

        binding.createCloseBtn.setOnClickListener { presenter.onCancelPressed() }
        binding.createLogoBtn.setOnClickListener { presenter.onPredefinedBtnPressed() }
        binding.createPredefinedBtn.setOnClickListener { presenter.onPredefinedBtnPressed() }
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        val date = LocalDate(year, month + 1, dayOfMonth)
        presenter.onPaymentDateInputChanged(date)
    }

    private fun setupFormInputs() = with(binding) {
        createTitleInput.doAfterTextChanged {
            presenter.onTitleInputChanged(it?.toString())
        }
        createPriceInput.doAfterTextChanged {
            presenter.onPriceInputChanged(it?.toString()?.toDoubleOrNull())
        }
        createPeriodQuantityInput.doAfterTextChanged {
            presenter.onPeriodQuantityInputChanged(it?.toString()?.toIntOrNull())
        }
        createTrialPeriodCheckbox.setOnCheckedChangeListener { _, isChecked ->
            presenter.onTrialPeriodChecked(isChecked)
        }
        createCategoryInput.doAfterTextChanged {
            presenter.onCategoryInputChanged(it?.toString())
        }
        createNoteInput.doAfterTextChanged {
            presenter.onCommentInputChanged(it?.toString())
        }
        createPaymentDateInput.setOnClickListener {
            presenter.onPaymentDateInputPressed()
        }
        createSubmitBtn.setOnClickListener {
            presenter.onSubmitPressed()
        }
    }

    override fun setupSuggestions(items: List<PredefinedSuggestionItem>) {
        val adapter = SuggestionsAdapter(items, binding.root.context)
        binding.createTitleInput.setAdapter(adapter)
        binding.createTitleInput.setOnItemClickListener { _, _, position, _ ->
            presenter.onSuggestionItemClick(items[position])
        }
    }

    override fun setSubLogo(logoUrl: String?) {
        if (logoUrl == null) {
            binding.createPredefinedBtn.show(true)
            binding.createLogoBtn.show(false)
        } else {
            binding.createPredefinedBtn.show(false)
            binding.createLogoBtn.show(true)

            Glide.with(binding.root.context)
                .load(logoUrl)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.createLogoBtn)
        }
    }

    override fun enableInputs(enable: Boolean) {
        binding.createTitleInput.isEnabled = enable
        binding.createPriceInput.isEnabled = enable
        binding.createPeriodQuantityInput.isEnabled = enable
        binding.createTrialPeriodCheckbox.isEnabled = enable
        binding.createPaymentDateInput.isEnabled = enable
        binding.createPeriodSelector.isEnabled = enable
        binding.createCategoryInput.isEnabled = enable
        binding.createNoteInput.isEnabled = enable
        binding.createSubmitBtn.isEnabled = enable
    }

    override fun openDatePicker(prevSelectedDate: LocalDate?) {
        val dateNow = LocalDate.now(DateTimeZone.forTimeZone(TimeZone.getDefault()))
        val selectedDate = prevSelectedDate ?: dateNow

        DatePickerDialog(
            binding.root.context, dateSetListener, selectedDate.year,
            selectedDate.monthOfYear - 1, selectedDate.dayOfMonth
        ).show()
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(binding.createBottomSheet)
    }

    override fun expandPanel() {
        binding.root.post {
            val offset = binding.root.context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(binding.root.height - offset, true)
        }
    }

    override fun collapsePanel() {
        binding.root.post {
            bottomSheet.setPeekHeight(0, true)
        }
    }

    override fun getPeriodString(period: SubscriptionPeriodType, quantity: Int): String {
        return binding.root.context.getSubPeriodString(period, quantity)
    }

    override fun setupSpinner(periods: List<String>) {
        val adapter = ArrayAdapter(binding.root.context, R.layout.spinner_item, periods)
        binding.createPeriodSelector.adapter = adapter
        binding.createPeriodSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no-op
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.onPeriodItemSelected(position)
            }
        }
    }

    override fun selectPeriodItem(position: Int) {
        binding.createPeriodSelector.setSelection(position, true)
    }

    override fun setCurrencySymbol(currency: Currency) {
        binding.createCurrencySymbol.text = currency.getCurrencySymbol()
    }

    override fun setEveryPlural(quantity: Int) {
        val text = binding.root.context.resources.getQuantityString(R.plurals.plural_every, quantity)
        binding.createEveryTextView.text = text
    }

    override fun setPaymentDateTrialDescription() {
        binding.createFirstPayDescription.text = binding.root.context.getString(R.string.desc_trial_pay)
    }

    override fun setPaymentDateDefaultDescription() {
        binding.createFirstPayDescription.text = binding.root.context.getString(R.string.desc_first_pay)
    }

    override fun setTrialPeriodCheckbox(checked: Boolean) {
        binding.createTrialPeriodCheckbox.isChecked = checked
    }

    override fun showToolbar(show: Boolean, duration: Long) {
        val fromAlpha = if (show) 0f else 1f
        val toAlpha = if (show) 1f else 0f
        binding.createToolbar.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun setBackgroundTransparent(transparent: Boolean, duration: Long) {
        val fromAlpha = if (transparent) 1f else 0f
        val toAlpha = if (transparent) 0f else 1f
        binding.root.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun hideKeyboard() {
        (binding.root.context as? AppCompatActivity).hideKeyboard()
    }

    override fun showTitleError(show: Boolean) {
        binding.createTitleInput.setError(show)
    }

    override fun showPriceError(show: Boolean) {
        val drawable = if (show) R.drawable.bg_edittext_error else R.drawable.bg_edittext
        binding.createPriceInput.background = ContextCompat.getDrawable(binding.root.context, drawable)
    }

    override fun showQuantityError(show: Boolean) {
        binding.createPeriodQuantityInput.setError(show)
    }

    override fun showPaymentDateError(show: Boolean) {
        binding.createPaymentDateInput.setError(show)
    }

    override fun setTitle(title: String) {
        binding.createTitleInput.text = SpannableStringBuilder(title)
    }

    override fun setPrice(price: Double) {
        binding.createPriceInput.text = SpannableStringBuilder(price.toString())
    }

    override fun setQuantity(quantity: Int) {
        binding.createPeriodQuantityInput.text = SpannableStringBuilder(quantity.toString())
    }

    override fun setDateInput(text: String) {
        binding.createPaymentDateInput.text = SpannableStringBuilder(text)
    }

    override fun setCategory(category: String) {
        binding.createCategoryInput.text = SpannableStringBuilder(category)
    }

    override fun setComment(note: String) {
        binding.createNoteInput.text = SpannableStringBuilder(note)
    }

    override fun switchTitlesToEditMode() {
        binding.createSubmitBtn.text = binding.root.context.getString(R.string.title_save)
        binding.createTitle.text = binding.root.context.getString(R.string.title_edit)
    }
}