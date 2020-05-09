package com.djavid.smartsubs.notification

import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.models.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.joda.time.LocalTime
import java.lang.NumberFormatException

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

    private fun setEditMode() {
        editMode = true
        timeChosen = true

        view.showDeleteBtn(true)
    }

    override fun onSubmitClicked() {
        if (validateForm()) {
            launch {
                if (editMode) {
                    repository.editNotification(model)
                } else {
                    repository.saveNotification(model.copy(active = true))
                }

                view.notifyToRefresh()
                view.finish()
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
        if (!timeChosen) {
            //todo try to add shake animation
            view.showTimeBtnError(true)
            view.setTimeBtnSelected(true)
            return false
        }

        if (model.daysBefore == -1L) {
            return false
        }

        return true
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

        if (model.isRepeating) {
            view.setRepeatButtonSelected(true)
        }

        if (timeChosen) {
            view.setTimeText(model.time.toString("HH:mm"))
            view.setTimeBtnSelected(true)
        }
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
        model = model.copy(daysBefore = 0)
        selectDayRadio()
    }

    override fun onDaysContainerClicked() {
        model = model.copy(daysBefore = daysInput)
        selectDaysRadio()
    }

    override fun onDayRadioChanged(checked: Boolean) {
        if (checked) {
            model = model.copy(daysBefore = 0)
            selectDayRadio()
        }
    }

    override fun onDaysRadioChanged(checked: Boolean) {
        if (checked) {
            model = model.copy(daysBefore = daysInput)
            selectDaysRadio()
        }
    }

    private fun selectDayRadio() {
        view.setDaysInputSelected(false)
        view.setDayRadioSelected(true)
        view.setDaysRadioSelected(false)
    }

    private fun selectDaysRadio() {
        view.setDaysInputSelected(true)
        view.setDayRadioSelected(false)
        view.setDaysRadioSelected(true)
    }

    override fun onDaysInputChanged(input: String?) {
        if (input != null) {
            try {
                daysInput = input.toLong()
                model = model.copy(daysBefore = daysInput)
                selectDaysRadio()
            } catch (e: NumberFormatException) {
                //no-op
            }
        }
    }

    override fun onTimeSet(time: LocalTime) {
        timeChosen = true
        model = model.copy(time = time)
        view.setTimeBtnSelected(true)
        view.showTimeBtnError(false)
        view.setTimeText(time.toString("HH:mm"))
    }

}