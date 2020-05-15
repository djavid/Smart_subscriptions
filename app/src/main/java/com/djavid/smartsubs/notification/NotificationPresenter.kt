package com.djavid.smartsubs.notification

import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.models.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.joda.time.LocalTime

class NotificationPresenter(
    private val view: NotificationContract.View,
    private val repository: NotificationsRepository,
    coroutineScope: CoroutineScope
) : NotificationContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var model: Notification
    private var editMode = false
    private var timeChosen = false
    private var daysInput = 1L

    override fun init(subscriptionId: Long, id: Long?) {
        view.init(this)

        launch {
            model = Notification(0, subscriptionId, false, -1, LocalTime(), false)

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
                //daysInput = model.daysBefore
                view.setDaysRadioSelected(true)
                view.setDaysInputText(model.daysBefore)
            }
        }

        view.setDaysPlural(daysInput)

        if (model.isRepeating) {
            view.setRepeatButtonSelected(true)
        }

        if (timeChosen) {
            view.setTimeText(model.time.toString("HH:mm"))
            view.setTimeBtnSelected(true)
        }
    }

    private fun setEditMode() {
        editMode = true
        timeChosen = true

        if (model.daysBefore > 1) {
            daysInput = model.daysBefore
        }

        view.showDeleteBtn(true)
        view.setSubmitButtonState(model.isActive)
    }

    override fun onSubmitClicked() {
        launch {
            if (editMode && !model.isActive) {
                model = model.copy(isActive = true)
                repository.editNotification(model)
                view.setSubmitButtonState(true)
                view.notifyToRefresh()
            } else {
                if (validateForm()) {
                    if (editMode) {
                        repository.editNotification(model)
                    } else {
                        repository.saveNotification(model.copy(isActive = true))
                    }

                    view.notifyToRefresh()
                    view.finish()
                }
            }
        }
    }

    override fun onDeleteClicked() {
        launch {
            repository.deleteNotificationById(model.id)
            view.notifyToRefresh()
            view.finish()
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        if (!timeChosen) {
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
        model = model.copy(daysBefore = 0)
        view.setDaysInputSelected(false)
        view.setDayRadioSelected(true)
        view.setDaysRadioSelected(false)
        view.clearFocus()
        view.hideKeyboard()
        view.setDaysInputError(false)
    }

    private fun selectDaysRadio() {
        model = model.copy(daysBefore = daysInput)
        view.setDaysInputSelected(true)
        view.setDayRadioSelected(false)
        view.setDaysRadioSelected(true)
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
        timeChosen = true
        model = model.copy(time = time)
        view.setTimeBtnSelected(true)
        view.showTimeBtnError(false)
        view.setTimeText(time.toString("HH:mm"))
    }

}