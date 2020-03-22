package com.djavid.smartsubs.create

import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.create.CreateView.Companion.SLIDE_DURATION
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import kotlinx.coroutines.*
import org.joda.time.DateTime
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
            SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1), null, null
        )

        val periods = periodItems.map { view.getPeriodString(it) }
        view.setupSpinner(periods)
        view.selectPeriodItem(periodItems.indexOf(model.period.type))
        view.setCurrencySymbol(model.currency)

        view.expandPanel()
        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)
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
        //todo add error showing
        if (model.title.isEmpty()) {
            return false
        }

        if (model.price <= 0) {
            return false
        }

        if (model.period.quantity <= 0) {
            return false
        }

        return true
    }

    override fun onPaymentDateInputPressed() {
        view.openDatePicker(model.paymentDate)
    }

    override fun onTitleInputChanged(input: String?) {
        model = model.copy(title = input ?: "")
    }

    override fun onPriceInputChanged(input: Double?) {
        model = model.copy(price = input ?: 0.0)
    }

    override fun onPeriodQuantityInputChanged(input: Int?) {
        model = model.copy(period = model.period.copy(quantity = input ?: 1))
    }

    override fun onPeriodItemSelected(position: Int) {
        val selectedPeriodType = periodItems[position]
        model = model.copy(period = model.period.copy(type = selectedPeriodType))
    }

    override fun onPaymentDateInputChanged(input: DateTime) {
        model = model.copy(paymentDate = input)
        view.setDateInput(input.toString("dd.MM.yyyy"))
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