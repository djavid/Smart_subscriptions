package com.djavid.smartsubs.create

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.models.*
import com.djavid.smartsubs.storage.CloudStorageRepository
import com.djavid.smartsubs.storage.RealTimeRepository
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.utils.DATE_TIME_FORMAT
import com.djavid.smartsubs.utils.SLIDE_DURATION
import kotlinx.android.synthetic.main.suggestion_item.view.*
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class CreatePresenter(
    private val view: CreateContract.View,
    private val repository: RealTimeRepository,
    private val fragmentNavigator: CommonFragmentNavigator,
    private val logger: FirebaseLogger,
    private val pipeline: BasePipeline<Pair<String, String>>,
    private val realTimeRepository: RealTimeRepository,
    private val storageRepository: CloudStorageRepository,
    coroutineScope: CoroutineScope
) : CreateContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var model: SubscriptionDao

    private var editMode = false
    private val periodItems = SubscriptionPeriodType.values().toList()
    private var isTrialSub: Boolean = false
    private val predefinedSubs = mutableListOf<PredefinedSubFirebaseEntity>()

    override fun init(id: String?) {
        view.init(this)

        view.enableInputs(false)
        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)
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
                fillForm(predefinedSubs.find { it.id == id })
            }

            updateSpinner()
            view.enableInputs(true)
            view.setSubLogo(null)
        }
    }

    private fun loadPredefinedSubs() = launch(Dispatchers.Main) {
        val matches = withContext(Dispatchers.IO) {
            predefinedSubs.addAll(realTimeRepository.getAllPredefinedSubs(allowCache = true))

            predefinedSubs.mapNotNull {
                val bytes = storageRepository.getSubLogoBytes(it.logoUrl) ?: return@mapNotNull null
                PredefinedSuggestionItem(it.id, it.title, bytes, it.abbreviations)
            }
        }

        view.setupSuggestions(matches)
    }

    override fun onSuggestionItemClick(item: PredefinedSuggestionItem) {
        model = model.copy(predefinedSubId = item.subId)
        view.setSubLogo(item.imageBytes)
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

    private fun fillForm(predefinedSub: PredefinedSubFirebaseEntity?) {
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

        loadSubLogo(predefinedSub)
    }

    private fun loadSubLogo(sub: PredefinedSubFirebaseEntity?) {
        launch {
            val bytes = withContext(Dispatchers.IO) {
                sub?.logoUrl?.let {
                    storageRepository.getSubLogoBytes(it)
                }
            }
            view.setSubLogo(bytes)
        }
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
            view.showToolbar(false, SLIDE_DURATION)
            view.setBackgroundTransparent(true, SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(SLIDE_DURATION) }
            pipeline.postValue(ACTION_REFRESH to "")
            fragmentNavigator.goBack()
        }
    }

    companion object {
        private const val MAX_TITLE_LENGTH = 30
        private const val MAX_PRICE_VALUE = 100000000
        private const val MAX_PERIOD_QUANTITY_VALUE = 10000
    }

}