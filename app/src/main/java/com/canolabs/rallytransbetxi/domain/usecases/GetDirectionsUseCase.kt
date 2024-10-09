package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.DirectionsRepositoryImpl
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import javax.inject.Inject

class GetDirectionsUseCase @Inject constructor(
    private val repository: DirectionsRepositoryImpl
) {
    fun execute(profile: DirectionsProfile, apiKey: String, start: String, end: String): List<List<Double>> {
        return repository.getDirections(profile.getDatabaseName(), apiKey, start, end)
    }
}