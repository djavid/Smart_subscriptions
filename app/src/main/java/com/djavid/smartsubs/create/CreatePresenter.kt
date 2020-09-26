package com.djavid.smartsubs.create

import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.utils.DATE_TIME_FORMAT
import com.djavid.smartsubs.utils.FirebaseLogger
import com.djavid.smartsubs.utils.SLIDE_DURATION
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class CreatePresenter(
    private val view: CreateContract.View,
    private val repository: SubscriptionsRepository,
    private val fragmentNavigator: CommonFragmentNavigator,
    private val logger: FirebaseLogger,
    private val pipeline: BasePipeline<Pair<String, String>>,
    coroutineScope: CoroutineScope
) : CreateContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var model: SubscriptionDao
    private var editMode = false
    private val periodItems = SubscriptionPeriodType.values().toList()
    private var isTrialSub: Boolean = false

    override fun init(id: Long?) {
        view.init(this)

        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)
        view.expandPanel()

        launch {
            model = SubscriptionDao(
                0, DateTime(),"", 0.0, Currency.getInstance("RUB"),
                SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1), null,
                null, null, null, false, null
            )

            if (id != null) {
                repository.getSubById(id)?.let { model = it }
                editMode = true

                view.switchTitlesToEditMode()
                fillForm()
            }

            updateSpinner()
        }
    }

    private fun fillForm() {
        view.setTitle(model.title)
        view.setPrice(model.price)
        view.setCurrencySymbol(model.currency)
        view.setQuantity(model.period.quantity)
        model.paymentDate?.let {
            view.setDateInput(it.toString(DATE_TIME_FORMAT))
        }
        view.setTrialPeriodCheckbox(model.trialPaymentDate != null)
        model.category?.let {
            view.setCategory(it)
        }
        model.comment?.let {
            view.setComment(it)
        }
    }

    private fun updateSpinner() {
        val periods = periodItems.map { view.getPeriodString(it, model.period.quantity) }
        view.setupSpinner(periods)
        view.selectPeriodItem(periodItems.indexOf(model.period.type))
    }

    override fun onSubmitPressed() {
        if (validateForm()) {
            launch {
                if (editMode) {
                    repository.editSub(model.copy(isLoaded = false))
                    logger.subEdited(model)
                } else {
                    repository.saveSub(model)
                    logger.subCreated(model)
                }

                finish()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (model.title.isEmpty()) {
            view.showTitleError(true)
            isValid = false
        }

        if (model.price <= 0) {
            view.showPriceError(true)
            isValid = false
        }

        if (model.period.quantity <= 0) {
            view.showQuantityError(true)
            isValid = false
        }

        model.trialPaymentDate?.let {
            if (it.isBefore(LocalDate.now())) {
                view.showPaymentDateError(true)
                isValid  = false
            }
        }

        return isValid
    }

    override fun onPaymentDateInputPressed() {
        view.openDatePicker(model.paymentDate)
    }

    override fun onTitleInputChanged(input: String?) {
        model = model.copy(title = input ?: "")
        view.showTitleError(false)
    }

    override fun onPriceInputChanged(input: Double?) {
        model = model.copy(price = input ?: 0.0)
        view.showPriceError(false)
    }

    override fun onPeriodQuantityInputChanged(input: Int?) {
        model = model.copy(period = model.period.copy(quantity = input ?: 1))
        view.showQuantityError(false)
        view.setEveryPlural(model.period.quantity)
        updateSpinner()
    }

    override fun onPeriodItemSelected(position: Int) {
        val selectedPeriodType = periodItems[position]
        model = model.copy(period = model.period.copy(type = selectedPeriodType))
    }

    override fun onTrialPeriodChecked(checked: Boolean) {
        view.showPaymentDateError(false)
        isTrialSub = checked

        model = if (checked) {
            view.setPaymentDateTrialDescription()
            model.copy(trialPaymentDate = model.paymentDate)
        } else {
            view.setPaymentDateDefaultDescription()
            model.copy(trialPaymentDate = null)
        }
    }

    override fun onPaymentDateInputChanged(input: LocalDate) {
        model = model.copy(paymentDate = input, trialPaymentDate = if (isTrialSub) input else null)
        view.showPaymentDateError(false)
        view.setDateInput(input.toString(DATE_TIME_FORMAT))
    }

    override fun onCategoryInputChanged(input: String?) {
        model = model.copy(category = input)
    }

    override fun onCommentInputChanged(input: String?) {
        model = model.copy(comment = input ?: "")
    }

    override fun onCancelPressed() {
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun finish() {
        launch {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, SLIDE_DURATION)
            view.setBackgroundTransparent(true, SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(SLIDE_DURATION) }
            pipeline.postValue(ACTION_REFRESH to "")
            fragmentNavigator.goBack()
        }
    }

}