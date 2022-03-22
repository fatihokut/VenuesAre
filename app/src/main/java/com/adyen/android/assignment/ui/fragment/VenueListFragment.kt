package com.adyen.android.assignment.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.R
import com.adyen.android.assignment.databinding.FragmentVenueListBinding
import com.adyen.android.assignment.ui.adapter.VenuesAdapter
import com.adyen.android.assignment.ui.viewmodel.VenuesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("VisibleForTests")
@AndroidEntryPoint
class VenueListFragment : Fragment() {
    companion object {
        private const val TAG = "VenueListFragment"
    }

    private var _binding: FragmentVenueListBinding? = null
    private val binding get() = _binding!!
    private val model: VenuesViewModel by lazy { ViewModelProvider(this)[VenuesViewModel::class.java] }
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        FusedLocationProviderClient(
            this.requireContext()
        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                handleGetVenues()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVenueListBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView = binding.venuesRecycler

        val adapter = VenuesAdapter()
        recyclerView.adapter = adapter

        model.venues.observe(viewLifecycleOwner) { venues ->
            if (venues != null) {
                adapter.submitList(venues)
            }
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

        return view
    }

    override fun onResume() {
        super.onResume()
        handleGetVenues()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun handleGetVenues() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if (it == null) {
                        requestNewLocationData()
                    } else {
                        model.getVenues(it.latitude, it.longitude)
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_retrieval_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showSnackbar()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            model.getVenues(
                locationResult.lastLocation.latitude,
                locationResult.lastLocation.longitude
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun showSnackbar() {
        Snackbar.make(
            requireView(),
            getString(R.string.permission_location_explanation),
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Ok",
            View.OnClickListener {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }).show()
    }

}