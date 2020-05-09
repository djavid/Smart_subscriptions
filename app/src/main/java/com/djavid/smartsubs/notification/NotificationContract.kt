package com.djavid.smartsubs.notification

import org.joda.time.LocalTime

interface NotificationContract {

    interface View {
        fun init(presenter: Presenter)
        fun setTimeBtnSelected(selected: Boolean)
        fun setRepeatButtonSelected(selected: Boolean)
        fun setDayRadioSelected(selected: Boolean)
        fun setDaysRadioSelected(selected: Boolean)
        fun setDaysInputText(days: Long)
        fun setTimeText(time: String)
        fun showTimePicker(hour: Int, minutes: Int)
        fun setDaysInputSelected(selected: Boolean)
        fun showTimeBtnError(show: Boolean)
        fun showDeleteBtn(show: Boolean)
        fun finish()
        fun notifyToRefresh()
    }

    interface Presenter {
        fun init(subscriptionId: Long, id: Long?)
        fun onTimeClicked()
        fun onRepeatClicked()
        fun onSubmitClicked()
        fun onDayContainerClicked()
        fun onDaysContainerClicked()
        fun onDayRadioChanged(checked: Boolean)
        fun onDaysRadioChanged(checked: Boolean)
        fun onDaysInputChanged(input: String?)
        fun onTimeSet(time: LocalTime)
        fun onDeleteClicked()
    }

    interface Navigator {
        fun showNotificationDialog(subscriptionId: Long, id: Long? = null)
    }

}