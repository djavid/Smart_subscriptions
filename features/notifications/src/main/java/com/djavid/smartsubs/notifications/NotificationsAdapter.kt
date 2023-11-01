package com.djavid.smartsubs.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.djavid.core.ui.R
import com.djavid.core.ui.databinding.NotificationItemBinding
import com.djavid.smartsubs.common.models.Notification
import com.djavid.smartsubs.common.utils.show

class NotificationsAdapter(
    private val context: Context,
    private val editAction: (Notification) -> Unit,
    private val checkChangedAction: (Notification, Boolean) -> Unit
) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    private var data = listOf<Notification>()

    fun setNotifications(items: List<Notification>) {
        data = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val time = item.atDateTime.toLocalTime().toString("HH:mm")
        val isInPaymentDay = item.daysBefore == 0L

        holder.checkBox.isChecked = item.isActive
        holder.repeatIcon.show(item.isRepeating)
        holder.editBtn.setOnClickListener { editAction.invoke(item) }
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            checkChangedAction(item, isChecked)
        }

        val dayPlural = context.resources.getQuantityString(R.plurals.plural_day, item.daysBefore.toInt())
        val notifText = if (isInPaymentDay)
            context.getString(R.string.title_in_payment_day_at, time)
        else
            context.getString(R.string.title_days_before_payment_at, item.daysBefore, dayPlural, time)
        holder.text.text = notifText
    }

    class ViewHolder(binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val checkBox: CheckBox = binding.notifCheckbox
        val repeatIcon: ImageView = binding.notifRepeatIcon
        val text: TextView = binding.notifText
        val editBtn: ImageView = binding.notifEditBtn
    }
}