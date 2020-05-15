package com.djavid.smartsubs.notification

import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.view.View
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.utils.setColor
import com.djavid.smartsubs.utils.setTintColor
import com.djavid.smartsubs.utils.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_notification.view.*
import org.joda.time.LocalTime

class NotificationView(
    private val viewRoot: View,
    private val fragment: Fragment
) : NotificationContract.View {

    private lateinit var presenter: NotificationContract.Presenter

    override fun init(presenter: NotificationContract.Presenter) {
        this.presenter = presenter

        viewRoot.notif_timeContainer.setOnClickListener {
            presenter.onTimeClicked()
        }
        viewRoot.notif_deleteBtn.setOnClickListener {
            presenter.onDeleteClicked()
        }
        viewRoot.notif_repeatContainer.setOnClickListener {
            presenter.onRepeatClicked()
        }
        viewRoot.notif_submitBtn.setOnClickListener {
            presenter.onSubmitClicked()
        }

        viewRoot.notif_dayContainer.setOnClickListener {
            presenter.onDayContainerClicked()
        }
        viewRoot.notif_dayRadio.setOnCheckedChangeListener { _, isChecked ->
            viewRoot.notif_dayRadio.setTintColor(if (isChecked) R.color.colorOrange else R.color.colorNero)
            presenter.onDayRadioChanged(isChecked)
        }

        viewRoot.notif_daysContainer.setOnClickListener {
            viewRoot.notif_daysInput.text
            presenter.onDaysContainerClicked()
        }
        viewRoot.notif_daysRadio.setOnCheckedChangeListener { _, isChecked ->
            viewRoot.notif_daysRadio.setTintColor(if (isChecked) R.color.colorOrange else R.color.colorNero)
            presenter.onDaysRadioChanged(isChecked)
        }
        viewRoot.notif_daysInput.doAfterTextChanged {
            presenter.onDaysInputChanged(it?.toString())
        }
        viewRoot.notif_daysInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                presenter.onDaysInputClicked()
            }
        }
    }

    override fun setDaysPlural(count: Long) {
        val days = viewRoot.context.resources.getQuantityString(R.plurals.plural_day, count.toInt())
        val text = viewRoot.context.getString(R.string.title_days_before, days)
        viewRoot.notif_daysSecondTitle.text = text
    }

    override fun setDaysInputError(show: Boolean) {
        viewRoot.notif_daysInput.setError(show)
    }

    override fun clearFocus() {
        viewRoot.notif_daysInput.clearFocus()
    }

    override fun hideKeyboard() {
        viewRoot.notif_daysInput.hideKeyBoard()
    }

    override fun showTimePicker(hour: Int, minutes: Int) {
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            presenter.onTimeSet(LocalTime(hourOfDay, minute))
        }

        TimePickerDialog(viewRoot.context, listener, hour, minutes, true).show()
    }

    override fun setTimeBtnSelected(selected: Boolean) {
        viewRoot.notif_timeContainer.alpha = if (selected) 1f else 0.6f
    }

    override fun setRepeatButtonSelected(selected: Boolean) {
        viewRoot.notif_repeatContainer.alpha = if (selected) 1f else 0.6f
    }

    override fun setDayRadioSelected(selected: Boolean) {
        viewRoot.notif_dayRadio.isChecked = selected
        viewRoot.notif_dayRadio.setTintColor(if (selected) R.color.colorOrange else R.color.colorNero)
    }

    override fun setDaysRadioSelected(selected: Boolean) {
        viewRoot.notif_daysRadio.isChecked = selected
        viewRoot.notif_daysRadio.setTintColor(if (selected) R.color.colorOrange else R.color.colorNero)
    }

    private fun RadioButton.setTintColor(colorRes: Int) {
        val color = ContextCompat.getColor(viewRoot.context, colorRes)
        buttonTintList = ColorStateList.valueOf(color)
    }

    override fun setDaysInputText(days: Long) {
        viewRoot.notif_daysInput.setText(days.toString())
    }

    override fun setTimeText(time: String) {
        viewRoot.notif_timeTitle.text = time
    }

    override fun setDaysInputSelected(selected: Boolean) {
        viewRoot.notif_daysInput.alpha = if (selected) 1f else 0.65f
    }

    override fun showTimeBtnError(show: Boolean) {
        val colorRes = if (show) R.color.colorPinkishOrange else R.color.colorNero
        viewRoot.notif_timeBtn.setTintColor(colorRes)
        viewRoot.notif_timeTitle.setColor(colorRes)
    }

    override fun showDeleteBtn(show: Boolean) {
        viewRoot.notif_deleteBtn.show(show)
    }

    override fun finish() {
        (fragment as? BottomSheetDialogFragment)?.dismiss()
    }

    override fun notifyToRefresh() {
        val intent = Intent(ACTION_REFRESH)
        LocalBroadcastManager.getInstance(viewRoot.context).sendBroadcast(intent)
    }

    @Suppress("deprecation")
    override fun setSubmitButtonState(active: Boolean) = with(viewRoot) {
        val bgRes = if (active) R.drawable.bg_submit_btn_active else R.drawable.bg_submit_btn_inactive
        val drawable = context.resources.getDrawable(bgRes, null)
        notif_submitBtn.background = drawable

        val textRes = if (active) R.string.title_save_notification else R.string.title_activate_notification
        val text = context.getString(textRes)
        notif_submitBtn.text = text
    }

}