package com.djavid.smartsubs.notification

import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.djavid.core.ui.R
import com.djavid.features.notification.databinding.FragmentNotificationBinding
import com.djavid.smartsubs.common.utils.setColor
import com.djavid.smartsubs.common.utils.setTintColor
import com.djavid.smartsubs.common.utils.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.joda.time.LocalTime

class NotificationView(
    private val fragment: Fragment,
    private val binding: FragmentNotificationBinding
) : NotificationContract.View {

    private lateinit var presenter: NotificationContract.Presenter
    private val context = binding.root.context

    override fun init(presenter: NotificationContract.Presenter) {
        this.presenter = presenter

        binding.notifTimeContainer.setOnClickListener {
            presenter.onTimeClicked()
        }
        binding.notifDeleteBtn.setOnClickListener {
            presenter.onDeleteClicked()
        }
        binding.notifRepeatContainer.setOnClickListener {
            presenter.onRepeatClicked()
        }
        binding.notifSubmitBtn.setOnClickListener {
            presenter.onSubmitClicked()
        }

        binding.notifDayContainer.setOnClickListener {
            presenter.onDayContainerClicked()
        }
        binding.notifDayRadio.setOnCheckedChangeListener { _, isChecked ->
            binding.notifDayRadio.setTintColor(if (isChecked) R.color.colorOrange else R.color.colorNero)
            presenter.onDayRadioChanged(isChecked)
        }

        binding.notifDaysContainer.setOnClickListener {
            binding.notifDaysInput.text
            presenter.onDaysContainerClicked()
        }
        binding.notifDaysRadio.setOnCheckedChangeListener { _, isChecked ->
            binding.notifDaysRadio.setTintColor(if (isChecked) R.color.colorOrange else R.color.colorNero)
            presenter.onDaysRadioChanged(isChecked)
        }
        binding.notifDaysInput.doAfterTextChanged {
            presenter.onDaysInputChanged(it?.toString())
        }
        binding.notifDaysInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                presenter.onDaysInputClicked()
            }
        }
    }

    override fun setDaysPlural(count: Long) {
        val days = context.resources.getQuantityString(R.plurals.plural_day, count.toInt())
        val text = context.getString(R.string.title_days_before, days)
        binding.notifDaysSecondTitle.text = text
    }

    override fun setDaysInputError(show: Boolean) {
        binding.notifDaysInput.setError(show)
    }

    override fun clearFocus() {
        binding.notifDaysInput.clearFocus()
    }

    override fun hideKeyboard() {
        binding.notifDaysInput.hideKeyBoard()
    }

    override fun showTimePicker(hour: Int, minutes: Int) {
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            presenter.onTimeSet(LocalTime(hourOfDay, minute))
        }

        TimePickerDialog(context, listener, hour, minutes, true).show()
    }

    override fun setTimeBtnSelected(selected: Boolean) {
        binding.notifTimeContainer.alpha = if (selected) 1f else 0.6f
    }

    override fun setRepeatButtonSelected(selected: Boolean) {
        binding.notifRepeatContainer.alpha = if (selected) 1f else 0.6f
    }

    override fun setDayRadioSelected(selected: Boolean) {
        binding.notifDayRadio.isChecked = selected
        binding.notifDayRadio.setTintColor(if (selected) R.color.colorOrange else R.color.colorNero)
    }

    override fun setDaysRadioSelected(selected: Boolean) {
        binding.notifDaysRadio.isChecked = selected
        binding.notifDaysRadio.setTintColor(if (selected) R.color.colorOrange else R.color.colorNero)
    }

    private fun RadioButton.setTintColor(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        buttonTintList = ColorStateList.valueOf(color)
    }

    override fun setDaysInputText(days: Long) {
        binding.notifDaysInput.setText(days.toString())
    }

    override fun setTimeText(time: String) {
        binding.notifTimeTitle.text = time
    }

    override fun setDaysInputSelected(selected: Boolean) {
        binding.notifDaysInput.alpha = if (selected) 1f else 0.65f
    }

    override fun showTimeBtnError(show: Boolean) {
        val colorRes = if (show) R.color.colorPinkishOrange else R.color.colorNero
        binding.notifTimeBtn.setTintColor(colorRes)
        binding.notifTimeTitle.setColor(colorRes)
    }

    override fun showDeleteBtn(show: Boolean) {
        binding.notifDeleteBtn.show(show)
    }

    override fun finish() {
        (fragment as? BottomSheetDialogFragment)?.dismiss()
    }

    @Suppress("deprecation")
    override fun setSubmitButtonState(active: Boolean) = with(binding) {
        val bgRes = if (active) R.drawable.bg_submit_btn_active else R.drawable.bg_submit_btn_inactive
        val drawable = context.resources.getDrawable(bgRes, null)
        notifSubmitBtn.background = drawable

        val textRes = if (active) R.string.title_save_notification else R.string.title_activate_notification
        val text = context.getString(textRes)
        notifSubmitBtn.text = text
    }

}