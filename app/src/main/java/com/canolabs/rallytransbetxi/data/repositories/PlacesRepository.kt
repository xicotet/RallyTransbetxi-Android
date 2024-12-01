package com.canolabs.rallytransbetxi.data.repositories
import android.util.Log
import com.canolabs.rallytransbetxi.data.models.requests.LocationRestriction
import com.canolabs.rallytransbetxi.data.models.requests.PlaceSearchRequest
import com.canolabs.rallytransbetxi.data.models.responses.PlaceResponse
import com.canolabs.rallytransbetxi.data.sources.remote.PlacesService
import com.canolabs.rallytransbetxi.utils.Constants.Companion.PLACES_FIELD_MASK
import javax.inject.Inject

interface PlacesRepository {
    suspend fun getBetxiRestaurants(
        apiKey: String,
        query: String,
        languageCode: String?,
        locationRestriction: LocationRestriction?
    ): List<PlaceResponse>
}

class PlacesRepositoryImpl @Inject constructor(
    private val placesService: PlacesService
) : PlacesRepository {

    override suspend fun getBetxiRestaurants(
        apiKey: String,
        query: String,
        languageCode: String?,
        locationRestriction: LocationRestriction?
    ): List<PlaceResponse> {
        Log.d("PlacesRepositoryImpl", "Fetching nearby restaurants for query: $query")

        val fieldMask = PLACES_FIELD_MASK

        val requestBody = PlaceSearchRequest(
            textQuery = query,
            languageCode = languageCode,
            locationRestriction = locationRestriction
        )

        return try {
            val response = placesService.searchRestaurants(apiKey, fieldMask, requestBody)
            response.places // Return the list of places
        } catch (e: Exception) {
            Log.e("PlacesRepositoryImpl", "Failed to fetch nearby restaurants", e)
            emptyList()
        }
    }

}