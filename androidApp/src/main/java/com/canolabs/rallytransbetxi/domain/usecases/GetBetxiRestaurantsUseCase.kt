package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.PlaceResponse
import com.canolabs.rallytransbetxi.data.repositories.PlacesRepositoryImpl
import javax.inject.Inject

class GetBetxiRestaurantsUseCase @Inject constructor(
    private val placesRepositoryImpl: PlacesRepositoryImpl
) {
    suspend operator fun invoke(
        apiKey: String,
        rankPreference: String,
        languageCode: String?,
    ): List<PlaceResponse> {
        return placesRepositoryImpl.getBetxiRestaurants(apiKey, rankPreference, languageCode)
    }
}