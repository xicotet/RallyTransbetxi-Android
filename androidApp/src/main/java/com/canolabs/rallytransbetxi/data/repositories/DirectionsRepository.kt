package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.sources.remote.DirectionsService

interface DirectionsRepository {
    fun getDirections(
        profile: String,
        apiKey: String,
        start: String,
        end: String
    ): List<List<Double>>
}

class DirectionsRepositoryImpl(
    private val service: DirectionsService
) : DirectionsRepository {
    override fun getDirections(
        profile: String,
        apiKey: String,
        start: String,
        end: String
    ): List<List<Double>> {
        Log.d("DirectionsRepositoryImpl", "Fetching directions")
        Log.d("DirectionsRepositoryImpl", "Profile: $profile")
        Log.d("DirectionsRepositoryImpl", "API Key: $apiKey")
        Log.d("DirectionsRepositoryImpl", "Start: $start")
        Log.d("DirectionsRepositoryImpl", "End: $end")
        val directions = service.getDirections(profile, apiKey, start, end).execute().body()
        if (directions == null) {
            Log.e("DirectionsRepositoryImpl", "Failed to fetch directions")
            return emptyList()
        }
        return directions.features.first().geometry.coordinates
    }
}