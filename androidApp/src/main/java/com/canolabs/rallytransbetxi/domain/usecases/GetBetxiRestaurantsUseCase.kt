package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.PlaceResponse
import com.canolabs.rallytransbetxi.data.repositories.PlacesRepository

class GetBetxiRestaurantsUseCase(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(
        rankPreference: String,
        languageCode: String?,
    ): List<PlaceResponse> {
        return placesRepository.getBetxiRestaurants(rankPreference, languageCode)
    }
}