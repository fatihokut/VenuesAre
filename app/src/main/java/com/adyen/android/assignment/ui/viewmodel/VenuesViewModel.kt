package com.adyen.android.assignment.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.ui.model.Venue
import com.adyen.android.assignment.ui.repository.VenuesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenuesViewModel @Inject internal constructor(
    private val repository: VenuesRepository
) : ViewModel() {

    private val _venues: MutableLiveData<ArrayList<Venue>> = repository.venues
    var venues: LiveData<ArrayList<Venue>> = _venues

    private val _isLoading = MutableLiveData(true)
    var isLoading: LiveData<Boolean> = _isLoading

    fun getVenues(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getVenueRecommendations(latitude, longitude)
            _isLoading.value = false
        }
    }

}