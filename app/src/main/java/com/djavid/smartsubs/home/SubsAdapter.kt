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
import com.djavid.smartsubs.models.getSymbolForCurrency
import com.djavid.smartsubs.utils.show
import kotlinx.android.synthetic.main.subscription_item.view.*
import java.text.DecimalFormat

class SubsAdapter(
    private val context: Context
) : RecyclerView.Adapter<SubsAdapter.ViewHolder>() {

    private var data = listOf<Subscription>()

    fun showSubs(subs: List<Subscription>) {
        data = subs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.subscription_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sub = data[position]
        val currencySymbol = context.getSymbolForCurrency(sub.currency)
        val df = DecimalFormat("0.##")

        holder.title.text = sub.title
        holder.price.text =
            context.getString(R.string.mask_price, df.format(sub.price), currencySymbol)

        sub.progress?.let {
            holder.progressBar.show(true)
            holder.periodLeft.show(true)

            val pluralDays = context.resources.getQuantityString(R.plurals.plural_days, it.daysLeft)
            holder.periodLeft.text = context.getString(R.string.mask_days_until, it.daysLeft, pluralDays)
            holder.progressBar.progress = (it.progress * 100).toInt()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.sub_title
        val price: TextView = itemView.sub_price
        val progressBar: ProgressBar = itemView.sub_progressBar
        val periodLeft: TextView = itemView.sub_periodLeft
    }

}