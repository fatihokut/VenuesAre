package com.adyen.android.assignment.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.databinding.VenueItemBinding
import com.adyen.android.assignment.ui.model.Venue
import com.bumptech.glide.Glide

class VenuesAdapter() :
    ListAdapter<Venue, VenuesAdapter.ViewHolder>(DiffCallback),
    Filterable {

    private var venueFilterList: List<Venue> = currentList

    // Filters by category name
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                venueFilterList = if (charSearch.isEmpty()) {
                    currentList
                } else {
                    currentList.filter {
                        it.categoryName.contains(charSearch, true)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = venueFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                venueFilterList = (results?.values as List<*>).filterIsInstance<Venue>()
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
            category.text = venueFilterList[position].categoryName
            distance.text = venueFilterList[position].distance.toString()
            loadImage(categoryImage, venueFilterList[position].iconUrl)
        }
    }

    override fun getItemCount() = venueFilterList.size

    override fun onCurrentListChanged(
        previousList: MutableList<Venue>,
        currentList: MutableList<Venue>
    ) {
        venueFilterList = currentList
    }

    companion object {
        @JvmStatic
        @BindingAdapter("categoryIcon")
        fun loadImage(view: ImageView, iconUrl: String?) {
            Log.d("VenuesAdapter", "ICON URL = $iconUrl")
            if (!iconUrl.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(iconUrl)
                    .into(view)
            }
        }
    }

}

object DiffCallback : DiffUtil.ItemCallback<Venue>() {

    override fun areItemsTheSame(oldItem: Venue, newItem: Venue) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Venue, newItem: Venue) =
        oldItem.name == newItem.name
}