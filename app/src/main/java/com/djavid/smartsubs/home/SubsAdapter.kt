package com.djavid.smartsubs.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Space
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djavid.smartsubs.R
import com.djavid.smartsubs.databinding.SubscriptionItemBinding
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.getPriceInPeriod
import com.djavid.smartsubs.models.getSymbolForCurrency
import com.djavid.smartsubs.utils.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.roundToInt

class SubsAdapter(
    private val context: Context,
    private val onClick: (String) -> Unit
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
        val view = SubscriptionItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.count()

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sub = data[position]

        Glide.with(holder.itemView.context)
            .load(sub.logoBytes)
            .into(holder.logo)
        holder.logo.show(sub.logoBytes != null)
        holder.title.text = sub.title
        holder.itemView.setOnClickListener { onClick(sub.id) }

        setupPrice(holder, sub)
        setupCategory(holder, sub)
        setupProgress(holder, sub)
    }

    private fun setupPrice(holder: ViewHolder, sub: Subscription) {
        val currencySymbol = context.getSymbolForCurrency(sub.price.currency)
        val priceForPeriod = sub.getPriceInPeriod(pricePeriod).roundToInt().toString()
        val priceColor = ContextCompat.getColor(
            context, if (sub.isTrial()) R.color.colorPinkishOrange else R.color.colorNero
        )

        holder.price.text = context.getString(R.string.mask_price, priceForPeriod, currencySymbol)
        holder.price.setTextColor(priceColor)
    }

    private fun setupProgress(holder: ViewHolder, sub: Subscription) {
        holder.progressBar.show(sub.progress != null)
        holder.periodLeft.show(sub.progress != null)

        sub.progress?.let { progress ->
            if (progress.daysLeft == 0) {
                holder.periodLeft.text = context.getString(R.string.title_today).replaceFirstChar { it.lowercase(context.getCurrentLocale()) }
            } else {
                val pluralDays = context.resources.getQuantityString(R.plurals.plural_day, progress.daysLeft)
                holder.periodLeft.text = context.getString(R.string.mask_days_until, progress.daysLeft, pluralDays)
            }

            holder.progressBar.progress = (progress.value * 100).toInt()
            holder.progressBar.setProgressColor(1 - progress.value)
        }
    }

    private fun setupCategory(holder: ViewHolder, sub: Subscription) {
        holder.category.show(sub.category != null)
        holder.categorySpace.show(sub.category != null)
        sub.category?.let {
            holder.category.text = it
            holder.categorySpace.show(true)
        }
    }

    private fun ProgressBar.setProgressColor(leftProgress: Double) {
        when {
            leftProgress >= CONST_GREEN_PROGRESS_MIN_PERCENT -> {
                progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_green_drawable)
            }
            leftProgress >= CONST_ORANGE_PROGRESS_MIN_PERCENT -> {
                progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_orange_drawable)
            }
            leftProgress >= CONST_RED_PROGRESS_MIN_PERCENT -> {
                progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_red_drawable)
            }
        }
    }

    class ViewHolder(binding: SubscriptionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val logo: CircleImageView = binding.subLogo
        val title: TextView = binding.subTitle
        val price: TextView = binding.subPrice
        val progressBar: ProgressBar = binding.subProgressBar
        val periodLeft: TextView = binding.subPeriodLeft
        val category: TextView = binding.subCategory
        val categorySpace: Space = binding.subCategorySpace
    }

}