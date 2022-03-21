package com.adyen.android.assignment.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.databinding.FragmentVenueListBinding
import com.adyen.android.assignment.ui.adapter.VenuesAdapter
import com.adyen.android.assignment.ui.viewmodel.VenuesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("VisibleForTests")
@AndroidEntryPoint
class VenueListFragment : Fragment() {
    companion object {
        private const val TAG = "VenueListFragment"
    }

    private var venueListBinding: FragmentVenueListBinding? = null
    private val model: VenuesViewModel by lazy { ViewModelProvider(this)[VenuesViewModel::class.java] }
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        FusedLocationProviderClient(
            this.requireContext()
        )
    }

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
        handleGetVenues()
    }

    override fun onDestroyView() {
        venueListBinding = null
        super.onDestroyView()
    }

    private fun handleGetVenues() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            model.getVenues(it.latitude, it.longitude)
        }
    }

    private fun requestLocationPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    handleGetVenues()
                } else {
                    // TODO: Message to explain why this permission is needed
                }
            }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                handleGetVenues()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

}