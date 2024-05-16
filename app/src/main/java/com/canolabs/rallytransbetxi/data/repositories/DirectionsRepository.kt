package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.sources.remote.DirectionsService
import javax.inject.Inject

interface DirectionsRepository {
    fun getDirections(apiKey: String, start: String, end: String): List<List<Double>>
}

class DirectionsRepositoryImpl @Inject constructor(
    private val service: DirectionsService
) : DirectionsRepository {
    override fun getDirections(apiKey: String, start: String, end: String): List<List<Double>> {
        val directions = service.getDirections(apiKey, start, end).execute().body()
            ?: throw Exception("Failed to fetch directions")
        return directions.features.first().geometry.coordinates
    }
}