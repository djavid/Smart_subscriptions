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
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.getPriceInPeriod
import com.djavid.smartsubs.models.getSymbolForCurrency
import com.djavid.smartsubs.utils.CONST_GREEN_PROGRESS_MIN_PERCENT
import com.djavid.smartsubs.utils.CONST_ORANGE_PROGRESS_MIN_PERCENT
import com.djavid.smartsubs.utils.CONST_RED_PROGRESS_MIN_PERCENT
import com.djavid.smartsubs.utils.show
import kotlinx.android.synthetic.main.subscription_item.view.*
import java.text.DecimalFormat

class SubsAdapter(
    private val context: Context
) : RecyclerView.Adapter<SubsAdapter.ViewHolder>() {

    private var data = listOf<Subscription>()
    lateinit var pricePeriod: SubscriptionPeriodType

    fun showSubs(subs: List<Subscription>) {
        data = subs
        notifyDataSetChanged()
    }

    fun updatePricePeriod(pricePeriod: SubscriptionPeriodType) {
        this.pricePeriod = pricePeriod
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
        val priceForPeriod = sub.getPriceInPeriod(pricePeriod)
        holder.price.text = context.getString(R.string.mask_price, df.format(priceForPeriod), currencySymbol)

        sub.progress?.let {

            holder.periodLeft.show(true)
            if (it.daysLeft == 0) {
                holder.periodLeft.text = context.getString(R.string.title_today)
            } else {
                val pluralDays = context.resources.getQuantityString(R.plurals.plural_day, it.daysLeft)
                holder.periodLeft.text = context.getString(R.string.mask_days_until, it.daysLeft, pluralDays)
            }

            holder.progressBar.show(true)
            holder.progressBar.progress = (it.progress * 100).toInt()
            holder.progressBar.setProgressColor(1 - it.progress)
        }
    }

    private fun ProgressBar.setProgressColor(leftProgress: Double) {
        when {
            leftProgress >= CONST_GREEN_PROGRESS_MIN_PERCENT -> {
                progressDrawable = context.getDrawable(R.drawable.progress_green_drawable)
            }
            leftProgress >= CONST_ORANGE_PROGRESS_MIN_PERCENT -> {
                progressDrawable = context.getDrawable(R.drawable.progress_orange_drawable)
            }
            leftProgress >= CONST_RED_PROGRESS_MIN_PERCENT -> {
                progressDrawable = context.getDrawable(R.drawable.progress_red_drawable)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.sub_title
        val price: TextView = itemView.sub_price
        val progressBar: ProgressBar = itemView.sub_progressBar
        val periodLeft: TextView = itemView.sub_periodLeft
    }

}