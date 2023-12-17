package com.djavid.smartsubs.currency_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.djavid.core.ui.R
import com.djavid.smartsubs.common.domain.PredefinedSubscription

class CurrencyListAdapter(
    private val onClick: (PredefinedSubscription) -> Unit
) : RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>() {

    private var data = listOf<PredefinedSubscription>()

    fun showSubs(subs: List<PredefinedSubscription>) {
        data = subs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.suggestion_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        Glide.with(holder.itemView.context)
            .load(item.logoUrl)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.suggestionLogo)
        holder.title.text = item.title
        holder.itemView.setOnClickListener { onClick.invoke(item) }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val suggestionLogo = itemView.findViewById<ImageView>(R.id.suggestionLogo)
        val title = itemView.findViewById<TextView>(R.id.text1)
    }
}