package com.djavid.smartsubs.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.getSubPeriodString
import kotlinx.android.synthetic.main.subscription_item.view.*

class SubsAdapter(
    private val context: Context,
    private val subs: List<Subscription>
) : RecyclerView.Adapter<SubsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.subscription_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = subs.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sub = subs[position]

        holder.title.text = sub.title
        holder.periodEnd.text = "до 23 мая 2020" //todo PrettyTime().format(Date(sub.periodEnd.utcEpochTime))
        holder.price.text = context.getString(R.string.mask_rub, sub.price)
        holder.pricePeriod.text = context.getString(
            R.string.mask_sub_period,
            context.getSubPeriodString(sub.period.type)
        )
        val plural = context.resources.getQuantityString(R.plurals.plural_days, sub.daysLeft)
        holder.periodLeft.text = context.getString(R.string.expires_in_days, sub.daysLeft, plural)
        holder.progressBar.progress = sub.progress
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.sub_title
        val periodEnd: TextView = itemView.sub_periodEnd
        val price: TextView = itemView.sub_price
        val pricePeriod: TextView = itemView.sub_pricePeriod
        val periodLeft: TextView = itemView.sub_periodLeft
        val progressBar: ProgressBar = itemView.sub_progressBar
    }

}