package com.djavid.smartsubs.create

import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.smartsubs.models.SubscriptionPeriodType
import org.joda.time.LocalDate
import java.util.*

interface CreateContract {

    interface Presenter {
        fun init(id: String?)
        fun onCancelPressed()
        fun onBackPressed()

        //form
        fun onTitleInputChanged(input: String?)

        fun onPriceInputChanged(input: Double?)

        fun onPeriodQuantityInputChanged(input: Int?)
        fun onPeriodItemSelected(position: Int)
        fun onTrialPeriodChecked(checked: Boolean)

        fun onPaymentDateInputPressed()
        fun onPaymentDateInputChanged(input: LocalDate)

        fun onCategoryInputChanged(input: String?)
        fun onCommentInputChanged(input: String?)

        fun onPredefinedBtnPressed()
        fun onSuggestionItemClick(item: PredefinedSuggestionItem)
        fun onSubmitPressed()
        fun onCurrencyClicked()
    }

    interface View {
        fun init(presenter: Presenter)
        fun getPeriodString(period: SubscriptionPeriodType, quantity: Int): String
        fun setupSpinner(periods: List<String>)
        fun expandPanel()
        fun enableInputs(enable: Boolean)
        fun openDatePicker(prevSelectedDate: LocalDate?)
        fun collapsePanel()
        fun showToolbar(show: Boolean, duration: Long)
        fun setBackgroundTransparent(transparent: Boolean, duration: Long)
        fun selectPeriodItem(position: Int)
        fun hideKeyboard()
        fun setDateInput(text: String)
        fun setCurrencySymbol(currency: Currency)
        fun setEveryPlural(quantity: Int)
        fun showTitleError(show: Boolean)
        fun showPriceError(show: Boolean)
        fun showQuantityError(show: Boolean)
        fun showPaymentDateError(show: Boolean)
        fun setTitle(title: String)
        fun setPrice(price: Double)
        fun setQuantity(quantity: Int)
        fun setCategory(category: String)
        fun setComment(note: String)
        fun switchTitlesToEditMode()
        fun setPaymentDateTrialDescription()
        fun setPaymentDateDefaultDescription()
        fun setTrialPeriodCheckbox(checked: Boolean)
        fun setSubLogo(bytes: ByteArray?)
        fun setupSuggestions(items: List<PredefinedSuggestionItem>)
    }

    interface Navigator {
        fun goToCreateScreen(id: String? = null)
    }

    companion object {
        const val REQUEST_GUID = "create_request_guid"
        const val RESULT_KEY_ = "create_request_guid"
    }

}