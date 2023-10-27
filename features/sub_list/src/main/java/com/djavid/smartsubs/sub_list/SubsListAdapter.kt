package com.djavid.smartsubs.sub_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import de.hdodenhof.circleimageview.CircleImageView

class SubsListAdapter(
    private val onClick: (PredefinedSuggestionItem) -> Unit
) : RecyclerView.Adapter<SubsListAdapter.ViewHolder>() {

    private var data = listOf<PredefinedSuggestionItem>()

    fun showSubs(subs: List<PredefinedSuggestionItem>) {
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

        Glide.with(holder.itemView.context).load(item.imageBytes).into(holder.suggestionLogo)
        holder.title.text = item.title
        holder.itemView.setOnClickListener { onClick.invoke(item) }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val suggestionLogo = itemView.findViewById<CircleImageView>(R.id.suggestionLogo)
        val title = itemView.findViewById<TextView>(R.id.text1)
    }
}