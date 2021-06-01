package com.djavid.smartsubs.create

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import kotlinx.android.synthetic.main.suggestion_item.view.*
import java.util.*

class SuggestionsAdapter(
    private val suggestions: List<PredefinedSuggestionItem>,
    context: Context
) : ArrayAdapter<PredefinedSuggestionItem>(context, R.layout.suggestion_item, suggestions) {

    private val suggestionsSource = mutableListOf<PredefinedSuggestionItem>().apply {
        addAll(suggestions)
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = convertView ?: layoutInflater.inflate(R.layout.suggestion_item, parent, false)
        Glide.with(parent.context)
            .load(item?.imageBytes)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view.suggestionLogo)
        view.text1.text = item?.title

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val subsSuggestion = mutableListOf<PredefinedSuggestionItem>()

                for (suggestion in suggestionsSource) {
                    suggestion.abbreviations.find { abbr ->
                        val match = constraint.toString().toLowerCase(Locale.ROOT)
                            .contains(abbr.toLowerCase(Locale.ROOT)) ||
                                abbr.toLowerCase(Locale.ROOT)
                                    .contains(constraint.toString().toLowerCase(Locale.ROOT))

                        match
                    }?.let {
                        subsSuggestion.add(suggestion)
                    }
                }
                filterResults.values = subsSuggestion
                filterResults.count = subsSuggestion.size

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val res = (results?.values as? MutableList<*>)?.mapNotNull {
                    it as? PredefinedSuggestionItem
                }

                res?.let {
                    clear()
                    addAll(it)

                    if (it.isNotEmpty()) {
                        notifyDataSetChanged()
                    } else {
                        notifyDataSetInvalidated()
                    }
                }
            }
        }
    }
}


