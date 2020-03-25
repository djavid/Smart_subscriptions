package com.djavid.smartsubs.create

import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.utils.SLIDE_DURATION
import kotlinx.coroutines.*
import org.joda.time.LocalDate
import java.util.*

class CreatePresenter(
    private val view: CreateContract.View,
    private val repository: SubscriptionsRepository,
    private val fragmentNavigator: CommonFragmentNavigator,
    coroutineScope: CoroutineScope
) : CreateContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var model: SubscriptionDao
    private val periodItems = SubscriptionPeriodType.values().toList()

    override fun init() {
        view.init(this)

        model = SubscriptionDao(
            0, "", 0.0, Currency.getInstance("RUB"),
            SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1),
            null, null, null
        )

        updateSpinner()
        view.setCurrencySymbol(model.currency)

        view.expandPanel()
        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)
    }

    private fun updateSpinner() {
        val periods = periodItems.map { view.getPeriodString(it, model.period.quantity) }
        view.setupSpinner(periods)
        view.selectPeriodItem(periodItems.indexOf(model.period.type))
    }

    override fun onSubmitPressed() {
        if (validateForm()) {
            launch {
                repository.saveSub(model)
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

    override fun onPaymentDateInputChanged(input: LocalDate) {
        model = model.copy(paymentDate = input)
        view.setDateInput(input.toString("dd.MM.yyyy"))
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
            view.notifyToRefreshSubs()
            fragmentNavigator.goBack()
        }
    }

}