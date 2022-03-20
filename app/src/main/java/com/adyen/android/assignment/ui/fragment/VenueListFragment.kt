package com.adyen.android.assignment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.databinding.FragmentVenueListBinding
import com.adyen.android.assignment.ui.adapter.VenuesAdapter
import com.adyen.android.assignment.ui.viewmodel.VenuesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VenueListFragment : Fragment() {
    companion object {
        private const val TAG = "VenueListFragment"
    }

    private var venueListBinding: FragmentVenueListBinding? = null
    private val model: VenuesViewModel by lazy { ViewModelProvider(this)[VenuesViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVenueListBinding.inflate(inflater, container, false)
        venueListBinding = binding

        val recyclerView = binding.venuesRecycler

        val adapter = VenuesAdapter()
        recyclerView.adapter = adapter

        model.venues.observe(viewLifecycleOwner) { venues ->
            adapter.submitList(venues)
        }

        binding.categorySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        model.getVenues()
    }

    override fun onDestroyView() {
        venueListBinding = null
        super.onDestroyView()
    }
}