package com.djavid.smartsubs.sort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.djavid.core.ui.R
import com.djavid.smartsubs.common.domain.SortBy
import com.djavid.ui.getTitle

class SortByAdapter(
    private val data: List<SortBy>,
    private val onClick: (SortBy) -> Unit
) : RecyclerView.Adapter<SortByAdapter.ViewHolder>() {

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sort_by, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = data[position]

        (holder.itemView as? TextView)?.text = item.getTitle(context)
        holder.itemView.setOnClickListener { onClick.invoke(item) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}