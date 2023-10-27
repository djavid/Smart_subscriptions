package com.djavid.smartsubs.create

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.common.BasePipeline
import com.djavid.common.CommonFragmentNavigator
import com.djavid.smartsubs.currencyList.CurrencyListContract
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionPeriod
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.data.storage.RealTimeRepository
import com.djavid.smartsubs.subList.SubListContract
import com.djavid.common.ACTION_REFRESH
import com.djavid.common.DATE_TIME_FORMAT
import com.djavid.common.SLIDE_DURATION
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class CreatePresenter(
    private val view: CreateContract.View,
    private val repository: com.djavid.data.storage.RealTimeRepository,
    private val fragmentNavigator: com.djavid.common.CommonFragmentNavigator,
    private val subListNavigator: SubListContract.Navigator,
    private val currencyListNavigator: CurrencyListContract.Navigator,
    private val logger: FirebaseLogger,
    private val pipelineString: com.djavid.common.BasePipeline<Pair<String, String>>,
    private val realTimeRepository: com.djavid.data.storage.RealTimeRepository,
    coroutineScope: CoroutineScope
) : CreateContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var model: SubscriptionDao

    private var editMode = false
    private val periodItems = SubscriptionPeriodType.values().toList()
    private var isTrialSub: Boolean = false
    private val predefinedSubs = mutableListOf<PredefinedSuggestionItem>()

    override fun init(id: String?) {
        view.init(this)

        view.enableInputs(false)
        view.setBackgroundTransparent(false, com.djavid.common.SLIDE_DURATION)
        view.showToolbar(true, com.djavid.common.SLIDE_DURATION)
        view.expandPanel()

        launch {
            model = SubscriptionDao(
                "", DateTime(), "", 0.0, Currency.getInstance("RUB"),
                SubscriptionPeriod(SubscriptionPeriodType.MONTH, 1), null,
                null, null, null, null
            )

            loadPredefinedSubs()

            if (id != null) {
                repository.getSubById(id)?.let { model = it }
                editMode = true

                view.switchTitlesToEditMode()
                fillForm(predefinedSubs.find { it.subId == model.predefinedSubId })
            } else {
                view.setSubLogo(null)
            }

            updateSpinner()
            view.enableInputs(true)
            view.setCurrencySymbol(model.currency)
        }
    }

    override fun onCurrencyClicked() {
        currencyListNavigator.goToCurrencyListScreen()
    }

    private suspend fun loadPredefinedSubs() = withContext(Dispatchers.Main) {
        predefinedSubs.clear()
        predefinedSubs.addAll(realTimeRepository.getAllPredefinedSubsWithLogo(allowCache = true))
        view.setupSuggestions(predefinedSubs)
    }

    override fun onSuggestionItemClick(item: PredefinedSuggestionItem) {
        model = model.copy(predefinedSubId = item.subId, title = item.title)
        view.setTitle(item.title)
        view.setSubLogo(item.imageBytes)
    }

    override fun onPredefinedBtnPressed() {
        subListNavigator.goToSubListScreen()
    }

    override fun onSubmitPressed() {
        if (validateForm()) {
            launch {
                if (editMode) {
                    repository.editSub(model)
                    logger.subEdited(model)
                } else {
                    repository.pushSub(model)
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
        view.setDateInput(input.toString(com.djavid.common.DATE_TIME_FORMAT))
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

    private fun fillForm(predefinedSub: PredefinedSuggestionItem?) {
        view.setTitle(model.title)
        view.setPrice(model.price)
        view.setCurrencySymbol(model.currency)
        view.setQuantity(model.period.quantity)
        model.paymentDate?.let {
            view.setDateInput(it.toString(com.djavid.common.DATE_TIME_FORMAT))
        }
        view.setTrialPeriodCheckbox(model.trialPaymentDate != null)
        model.category?.let {
            view.setCategory(it)
        }
        model.comment?.let {
            view.setComment(it)
        }

        view.setSubLogo(predefinedSub?.imageBytes)
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
            view.showToolbar(false, com.djavid.common.SLIDE_DURATION)
            view.setBackgroundTransparent(true, com.djavid.common.SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(com.djavid.common.SLIDE_DURATION) }
//            fragmentNavigator.setFragmentResult(CreateContract.REQUEST_GUID, Bundle().apply {
//                putSerializable("", )
//            })
            pipelineString.postValue(com.djavid.common.ACTION_REFRESH to "")
            fragmentNavigator.goBack()
        }
    }

    companion object {
        private const val MAX_TITLE_LENGTH = 30
        private const val MAX_PRICE_VALUE = 100000000
        private const val MAX_PERIOD_QUANTITY_VALUE = 10000
    }

}