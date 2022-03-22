package com.adyen.android.assignment.ui.repository

import androidx.lifecycle.MutableLiveData
import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.PlacesService.Companion.instance
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.ui.model.Venue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VenuesRepository @Inject constructor() {
    companion object {
        private const val TAG = "VenuesRepository"
        private const val iconSize = "64"
    }

    val venues = MutableLiveData<ArrayList<Venue>>()
    var client: PlacesService = instance

    suspend fun getVenueRecommendations(latitude: Double, longitude: Double) =
        client.getVenueRecommendations(
            VenueRecommendationsQueryBuilder()
                .setLatitudeLongitude(latitude, longitude)
                .build()
        ).let {
            venues.postValue(buildResults(it))
        }

    private fun buildResults(wrapper: ResponseWrapper): ArrayList<Venue> {
        wrapper.let { response ->
            val resultsList = ArrayList<Venue>()
            response.results?.forEach {
                resultsList.add(
                    Venue(
                        it.name,
                        if (!it.categories.isNullOrEmpty()) {
                            it.categories[0].name
                        } else {
                            "Unknown category"
                        },
                        it.distance,
                        if (!it.categories.isNullOrEmpty()) {
                            it.categories[0].icon.prefix + iconSize + it.categories[0].icon.suffix
                        } else {
                            ""
                        }
                    )
                )
            }

            return ArrayList(resultsList.sortedBy { it.distance })
        }
    }

}