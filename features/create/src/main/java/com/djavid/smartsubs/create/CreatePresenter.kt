package com.djavid.smartsubs.create

import androidx.lifecycle.LifecycleCoroutineScope
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.base.BasePipeline
import com.djavid.smartsubs.common.navigation.CommonFragmentNavigator
import com.djavid.smartsubs.common.navigation.CurrencyListNavigator
import com.djavid.smartsubs.common.navigation.SubListNavigator
import com.djavid.smartsubs.common.domain.PredefinedSubscription
import com.djavid.smartsubs.common.domain.Subscription
import com.djavid.smartsubs.common.domain.SubscriptionPeriod
import com.djavid.smartsubs.common.domain.SubscriptionPeriodType
import com.djavid.smartsubs.data.storage.RealTimeRepository
import com.djavid.smartsubs.common.utils.Constants
import com.djavid.smartsubs.data.storage.PredefinedSubscriptionRepository
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class CreatePresenter(
    private val view: CreateContract.View,
    private val realTimeRepository: RealTimeRepository,
    private val predefinedSubscriptionRepository: PredefinedSubscriptionRepository,
    private val fragmentNavigator: CommonFragmentNavigator,
    private val subListNavigator: SubListNavigator,
    private val currencyListNavigator: CurrencyListNavigator,
    private val logger: FirebaseLogger,
    private val pipelineString: BasePipeline<Pair<String, String>>,
    coroutineScope: LifecycleCoroutineScope
) : CreateContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var model: Subscription

    private var editMode = false
    private val periodItems = SubscriptionPeriodType.entries
    private var isTrialSub: Boolean = false

    override fun init(id: String?) {
        view.init(this)

        view.enableInputs(false)
        view.setBackgroundTransparent(false, Constants.SLIDE_DURATION)
        view.showToolbar(true, Constants.SLIDE_DURATION)
        view.expandPanel()

        launch {
            model = Subscription(
                "", DateTime(), "", 0.0, Currency.getInstance("RUB"),
                SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1), null,
                null, null, null, null
            )

            if (id != null) {
                realTimeRepository.getSubById(id)?.let { model = it }
                editMode = true
                view.switchTitlesToEditMode()

                val predefinedSub = model.predefinedSubId?.let { id ->
                    predefinedSubscriptionRepository.getPredefinedSub(id)
                }

                if (predefinedSub != null) {
                    fillForm(predefinedSub)
                }
            } else {
                view.setSubLogo(null)
            }

            updateSpinner()
            view.enableInputs(true)
            view.setCurrencySymbol(model.currency)
        }
    }

    override fun onCurrencyClicked() {
        //todo release 1.1
        //currencyListNavigator.goToCurrencyListScreen()
    }

    override fun onSuggestionItemClick(item: PredefinedSubscription) {
        model = model.copy(predefinedSubId = item.subId, title = item.title)
        view.setTitle(item.title)
        view.setSubLogo(item.logoUrl)
    }

    override fun onPredefinedSubChosen(id: String) {
        launch {
            predefinedSubscriptionRepository.getPredefinedSub(id)?.let {
                onSuggestionItemClick(it)
            }
        }
    }

    override fun onPredefinedBtnPressed() {
        subListNavigator.goToSubListScreen()
    }

    override fun onSubmitPressed() {
        if (validateForm()) {
            launch {
                if (editMode) {
                    realTimeRepository.editSub(model)
                    logger.subEdited(model)
                } else {
                    realTimeRepository.pushSub(model)
                    logger.subCreated(model)
                }

                finish()
            }
        }
    }

    override fun onPaymentDateInputPressed() {
        view.openDatePicker(model.paymentDate)
    }

    override fun onTitleInputChanged(input: String?) {
        if (model.title == input) return
        model = model.copy(title = input ?: "", predefinedSubId = null)
        view.showTitleError(false)
        view.setSubLogo(null)
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
        view.setDateInput(input.toString(Constants.DATE_TIME_FORMAT))
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

    private fun validateForm(): Boolean {
        var isValid = true

        if (model.title.isEmpty() || model.title.length > MAX_TITLE_LENGTH) {
            view.showTitleError(true)
            isValid = false
        }

        if (model.price <= 0 || model.price > MAX_PRICE_VALUE) {
            view.showPriceError(true)
            isValid = false
        }

        if (model.period.quantity <= 0 || model.period.quantity > MAX_PERIOD_QUANTITY_VALUE) {
            view.showQuantityError(true)
            isValid = false
        }

        model.trialPaymentDate?.let {
            if (it.isBefore(LocalDate.now())) {
                view.showPaymentDateError(true)
                isValid = false
            }
        }

        return isValid
    }

    private fun fillForm(predefinedSub: PredefinedSubscription?) {
        view.setTitle(model.title)
        view.setPrice(model.price)
        view.setCurrencySymbol(model.currency)
        view.setQuantity(model.period.quantity)
        model.paymentDate?.let {
            view.setDateInput(it.toString(Constants.DATE_TIME_FORMAT))
        }
        view.setTrialPeriodCheckbox(model.trialPaymentDate != null)
        model.category?.let {
            view.setCategory(it)
        }
        model.comment?.let {
            view.setComment(it)
        }

        view.setSubLogo(predefinedSub?.logoUrl)
    }

    private fun updateSpinner() {
        val periods = periodItems.map { view.getPeriodString(it, model.period.quantity) }
        view.setupSpinner(periods)
        view.selectPeriodItem(periodItems.indexOf(model.period.type))
    }

    private fun finish() {
        launch {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, Constants.SLIDE_DURATION)
            view.setBackgroundTransparent(true, Constants.SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(Constants.SLIDE_DURATION) }
//            fragmentNavigator.setFragmentResult(CreateContract.REQUEST_GUID, Bundle().apply {
//                putSerializable("", )
//            })
            pipelineString.postValue(Constants.ACTION_REFRESH to "")
            fragmentNavigator.goBack()
        }
    }

    companion object {
        private const val MAX_TITLE_LENGTH = 30
        private const val MAX_PRICE_VALUE = 100000000
        private const val MAX_PERIOD_QUANTITY_VALUE = 10000
    }
}