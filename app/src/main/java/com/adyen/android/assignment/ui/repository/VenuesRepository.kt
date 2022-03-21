package com.adyen.android.assignment.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.ui.model.Venue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VenuesRepository @Inject constructor() {
    companion object {
        private const val TAG = "VenuesRepository"
    }

    val venues = MutableLiveData<ArrayList<Venue>>()

    fun getVenueRecommendations(latitude: Double, longitude: Double) {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(latitude, longitude)
            .build()

        PlacesService.instance.getVenueRecommendations(query).enqueue(object :
            Callback<ResponseWrapper> {
            override fun onFailure(call: Call<ResponseWrapper>, t: Throwable) {
                Log.d(TAG, "Unsuccessful request: " + t.message)
            }

            override fun onResponse(
                call: Call<ResponseWrapper>,
                response: Response<ResponseWrapper>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "Success: " + response.raw().request().url().toString())
                    response.body()?.let { it ->
                        venues.postValue(buildResults(it))
                    }
                } else {
                    onFailure(
                        call,
                        Throwable("Unsuccessful request: " + response.code())
                    )
                }
            }
        })

    }

    private fun buildResults(wrapper: ResponseWrapper): ArrayList<Venue> {
        wrapper.let { response ->
            val resultsList = ArrayList<Venue>()
            response.results?.forEach {
                resultsList.add(
                    Venue(
                        it.name,
                        it.categories[0].name,
                        it.distance,
                        it.categories[0].icon.prefix + "64" + it.categories[0].icon.suffix
                    )
                )
            }

            return ArrayList(resultsList.sortedBy { it.distance })
        }
    }

}