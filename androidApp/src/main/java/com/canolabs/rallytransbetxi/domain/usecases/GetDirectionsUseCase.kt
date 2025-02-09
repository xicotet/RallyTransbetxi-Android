package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.DirectionsRepository
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile

class GetDirectionsUseCase(
    private val repository: DirectionsRepository
) {
    fun execute(profile: DirectionsProfile, apiKey: String, start: String, end: String): List<List<Double>> {
        return repository.getDirections(profile.getDatabaseName(), apiKey, start, end)
    }
}