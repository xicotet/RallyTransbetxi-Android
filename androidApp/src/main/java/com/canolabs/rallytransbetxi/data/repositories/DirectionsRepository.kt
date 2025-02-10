package com.canolabs.rallytransbetxi.data.repositories

import android.util.Log
import com.canolabs.rallytransbetxi.data.sources.remote.DirectionsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DirectionsRepository {
    suspend fun getDirections(
        profile: String,
        start: String,
        end: String
    ): List<List<Double>>
}

class DirectionsRepositoryImpl(
    private val service: DirectionsService
) : DirectionsRepository {

    override suspend fun getDirections(
        profile: String,
        start: String,
        end: String
    ): List<List<Double>> = withContext(Dispatchers.IO) { // Ensure network call is on IO thread
        try {
            Log.d("DirectionsRepositoryImpl", "Fetching directions")
            val directions = service.getDirections(profile, start, end)
            directions.features.firstOrNull()?.geometry?.coordinates ?: emptyList()
        } catch (e: Exception) {
            Log.e("DirectionsRepositoryImpl", "Failed to fetch directions: ${e.message}", e)
            emptyList()
        }
    }
}