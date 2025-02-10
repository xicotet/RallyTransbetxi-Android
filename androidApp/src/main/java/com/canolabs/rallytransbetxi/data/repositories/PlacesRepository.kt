package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.requests.Circle
import com.canolabs.rallytransbetxi.data.models.requests.LocationRestriction
import com.canolabs.rallytransbetxi.data.models.requests.PlaceSearchRequest
import com.canolabs.rallytransbetxi.data.models.responses.LatLng
import com.canolabs.rallytransbetxi.data.models.responses.PlaceResponse
import com.canolabs.rallytransbetxi.data.sources.remote.PlacesService
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.Constants.Companion.PLACES_FIELD_MASK
import com.canolabs.rallytransbetxi.utils.Constants.Companion.PLACES_NEARBY_SEARCH_EXCLUDED_TYPES
import com.canolabs.rallytransbetxi.utils.Constants.Companion.PLACES_NEARBY_SEARCH_RADIUS

interface PlacesRepository {
    suspend fun getBetxiRestaurants(
        rankPreference: String,
        languageCode: String?,
    ): List<PlaceResponse>
}

class PlacesRepositoryImpl(
    private val placesService: PlacesService
) : PlacesRepository {

    override suspend fun getBetxiRestaurants(
        rankPreference: String,
        languageCode: String?,
    ): List<PlaceResponse> {
        Log.d("PlacesRepositoryImpl", "Fetching nearby restaurants")

        val fieldMask = PLACES_FIELD_MASK

        val betxiLocation = Constants.BETXI_LOCATION.split(",")
        val betxi = LatLng(
            betxiLocation[0].toDouble(),
            betxiLocation[1].toDouble()
        )

        val requestBody = PlaceSearchRequest(
            includedTypes = listOf("restaurant", "bar"), // TODO: Extract to ENUM or SEALED CLASS to further include more types
            excludedTypes = PLACES_NEARBY_SEARCH_EXCLUDED_TYPES.split(","),
            languageCode = languageCode,
            rankPreference = rankPreference,
            locationRestriction = LocationRestriction(circle = Circle(center = betxi, radius = PLACES_NEARBY_SEARCH_RADIUS))
        )

        return try {
            val response = placesService.searchNearbyRestaurants(fieldMask, requestBody)
            response.places ?: emptyList() // Return the list of places
        } catch (e: Exception) {
            Log.e("PlacesRepositoryImpl", "Failed to fetch nearby restaurants", e)
            emptyList()
        }
    }
}