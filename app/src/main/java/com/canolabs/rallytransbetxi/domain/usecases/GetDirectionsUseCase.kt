package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.DirectionsRepositoryImpl
import javax.inject.Inject

class GetDirectionsUseCase @Inject constructor(
    private val repository: DirectionsRepositoryImpl
) {
    fun execute(apiKey: String, start: String, end: String): List<List<Double>> {
        return repository.getDirections(apiKey, start, end)
    }
}