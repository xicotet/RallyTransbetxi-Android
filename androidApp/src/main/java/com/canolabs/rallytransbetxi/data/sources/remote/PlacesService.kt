package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.requests.PlaceSearchRequest
import com.canolabs.rallytransbetxi.data.models.responses.PlaceSearchResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PlacesService {

    @POST("./places:searchNearby") // The './' notation is needed so retrofit can handle correctly the colon
    suspend fun searchNearbyRestaurants(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String,
        @Body body: PlaceSearchRequest
    ): PlaceSearchResponse
}