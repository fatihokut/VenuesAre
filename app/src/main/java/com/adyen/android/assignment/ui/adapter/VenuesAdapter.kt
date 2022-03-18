package com.adyen.android.assignment.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.databinding.VenueItemBinding
import com.adyen.android.assignment.ui.model.Venue
import java.util.*

class VenuesAdapter(private val venueList: ArrayList<Venue>) :
    RecyclerView.Adapter<VenuesAdapter.ViewHolder>(), Filterable {

    var venueFilterList = ArrayList<Venue>()

    init {
        venueFilterList = venueList
    }

    // TODO: Filter by venue name for now. It'll be category name eventually
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    venueFilterList = venueList
                } else {
                    val resultList = ArrayList<Venue>()
                    for (row in venueList) {
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    venueFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = venueFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                venueFilterList = results?.values as ArrayList<Venue>
                notifyDataSetChanged()
            }

        }
    }

    inner class ViewHolder(val binding: VenueItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VenueItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.binding.apply {
            venueName.text = venueFilterList[position].name
            category.text = venueFilterList[position].categoryId.toString()
            distance.text = venueFilterList[position].distance.toString()
        }
    }

    override fun getItemCount() = venueFilterList.size

}
