package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.DirectionsRepository
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile

class GetDirectionsUseCase(
    private val repository: DirectionsRepository
) {
    suspend fun execute(profile: DirectionsProfile, start: String, end: String): List<List<Double>> {
        return repository.getDirections(profile.getDatabaseName(), start, end)
    }
}