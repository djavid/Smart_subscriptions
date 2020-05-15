package com.djavid.smartsubs.subscription

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.utils.show
import kotlinx.android.synthetic.main.notification_item.view.*

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
        val view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val time = item.time.toString("HH:mm")
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.notif_checkbox
        val repeatIcon: ImageView = itemView.notif_repeatIcon
        val text: TextView = itemView.notif_text
        val editBtn: ImageView = itemView.notif_editBtn
    }
}