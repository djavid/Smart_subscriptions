package com.djavid.smartsubs.notification

import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.utils.getFirstPeriodAfterNow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalTime

class NotificationPresenter(
    private val view: NotificationContract.View,
    private val repository: NotificationsRepository,
    private val alarmNotifier: AlarmNotifier,
    private val pipeline: BasePipeline<Pair<String, String>>,
    private val logger: FirebaseLogger,
    private val subMapper: SubscriptionModelMapper,
    coroutineScope: CoroutineScope
) : NotificationContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var model: Notification
    private var subModel: SubscriptionDao? = null
    private var editMode = false
    private var chosenTime: LocalTime? = null
    private var daysInput = 1L

    override fun init(subscriptionId: Long, id: Long?) {
        view.init(this)

        launch {
            model = Notification(
                0, subscriptionId, DateTime(), false, -1, DateTime(), false
            )

            if (id != null) {
                repository.getNotificationById(id)?.let { model = it }
                setEditMode()
            }

            fillForm()
        }
    }

    private fun fillForm() {
        when {
            model.daysBefore == 0L -> {
                view.setDayRadioSelected(true)
            }
            model.daysBefore > 0 -> {
                view.setDaysRadioSelected(true)
                view.setDaysInputText(model.daysBefore)
            }
        }

        view.setDaysPlural(daysInput)

        if (model.isRepeating) {
            view.setRepeatButtonSelected(true)
        }

        chosenTime?.let {
            view.setTimeText(it.toString("HH:mm"))
            view.setTimeBtnSelected(true)
        }
    }

    private fun setEditMode() {
        editMode = true

        if (model.daysBefore > 1) {
            daysInput = model.daysBefore
        }

        chosenTime = model.atDateTime.toLocalTime()

        view.showDeleteBtn(true)
        view.setSubmitButtonState(model.isActive)
    }

    override fun onSubmitClicked() {
        launch {
            if (validateForm()) {
                if (editMode && !model.isActive) {
                    activateNotification()
                } else {
                    saveNotification()
                }

                pipeline.postValue(ACTION_REFRESH to "")
                alarmNotifier.setAlarm(model)
                view.finish()
            }
        }
    }

    private suspend fun saveNotification() {
        if (editMode) {
            repository.editNotification(model)
            logger.onNotifEdited(model)
        } else {
            repository.saveNotification(model.copy(isActive = true))
            logger.onNotifCreated(model)
        }
    }

    private suspend fun activateNotification() {
        model = model.copy(isActive = true)
        repository.editNotification(model)
        logger.onActivateNotifClicked(model)
    }

    override fun onDeleteClicked() {
        launch {
            alarmNotifier.cancelAlarm(model.id)
            repository.deleteNotificationById(model.id)
            pipeline.postValue(ACTION_REFRESH to "")
            subModel?.let {
                logger.subDelete(subMapper.fromDao(it))
            }
            view.finish()
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        if (chosenTime == null) {
            //todo try to add shake animation
            view.showTimeBtnError(true)
            view.setTimeBtnSelected(true)
            valid = false
        }

        if (model.daysBefore == -1L) {
            view.setDaysInputError(true)
            valid = false
        }

        return valid
    }

    override fun onTimeClicked() {
        val time = LocalTime()
        view.showTimePicker(time.hourOfDay, time.minuteOfHour)
    }

    override fun onRepeatClicked() {
        model = model.copy(isRepeating = !model.isRepeating)
        view.setRepeatButtonSelected(model.isRepeating)
    }

    override fun onDayContainerClicked() {
        view.setDayRadioSelected(true)
    }

    override fun onDaysContainerClicked() {
        view.setDaysRadioSelected(true)
    }

    override fun onDayRadioChanged(checked: Boolean) {
        if (checked) {
            selectDayRadio()
        }
    }

    override fun onDaysRadioChanged(checked: Boolean) {
        if (checked) {
            selectDaysRadio()
        }
    }

    private fun selectDayRadio() {
        launch {
            model = model.copy(daysBefore = 0)
            calculateDateTime()

            view.setDaysInputSelected(false)
            view.setDayRadioSelected(true)
            view.setDaysRadioSelected(false)
            view.clearFocus()
            view.hideKeyboard()
            view.setDaysInputError(false)
        }
    }

    private fun selectDaysRadio() {
        launch {
            model = model.copy(daysBefore = daysInput)
            calculateDateTime()

            view.setDaysInputSelected(true)
            view.setDayRadioSelected(false)
            view.setDaysRadioSelected(true)
        }
    }

    override fun onDaysInputChanged(input: String?) {
        if (input != null && input.isNotEmpty()) {
            try {
                daysInput = input.toLong()
                view.setDaysPlural(daysInput)
                view.setDaysInputError(false)
            } catch (e: NumberFormatException) {
                //no-op
            }
        } else {
            daysInput = -1
        }

        model = model.copy(daysBefore = daysInput)
        selectDaysRadio()
    }

    override fun onDaysInputClicked() {
        model = model.copy(daysBefore = daysInput)
        selectDaysRadio()
    }

    override fun onTimeSet(time: LocalTime) {
        launch {
            chosenTime = time
            calculateDateTime()

            view.setTimeBtnSelected(true)
            view.showTimeBtnError(false)
            view.setTimeText(time.toString("HH:mm"))
        }
    }

    private suspend fun calculateDateTime() {
        withContext(Dispatchers.IO) {
            val paymentDate = subModel?.paymentDate
            val period = subModel?.period

            if (paymentDate != null && period != null && chosenTime != null) {
                val nextPaymentDate = paymentDate.getFirstPeriodAfterNow(period)
                val atDateTime = nextPaymentDate.minusDays(model.daysBefore.toInt()).toDateTime(chosenTime)
                model = model.copy(atDateTime = atDateTime)
            }
        }
    }

}