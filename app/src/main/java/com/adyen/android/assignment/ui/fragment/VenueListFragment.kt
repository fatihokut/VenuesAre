package com.adyen.android.assignment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.adyen.android.assignment.databinding.FragmentVenueListBinding
import com.adyen.android.assignment.ui.adapter.VenuesAdapter
import com.adyen.android.assignment.ui.model.SampleVenues

class VenueListFragment : Fragment() {

    private var venueListBinding: FragmentVenueListBinding? = null

    private val sampleVenues = SampleVenues()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVenueListBinding.inflate(inflater, container, false)
        venueListBinding = binding

        val venuesList = sampleVenues.initVenueList()
        val venuesAdapter = VenuesAdapter(venuesList)
        val recyclerView = binding.venuesRecycler
        recyclerView.adapter = venuesAdapter

        binding.categorySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                venuesAdapter.filter.filter(newText)
                return false
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        venueListBinding = null
        super.onDestroyView()
    }
}