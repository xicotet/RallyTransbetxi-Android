package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.requests.LocationRestriction
import com.canolabs.rallytransbetxi.data.models.responses.PlaceResponse
import com.canolabs.rallytransbetxi.data.repositories.PlacesRepositoryImpl
import javax.inject.Inject

class GetBetxiRestaurantsUseCase @Inject constructor(
    private val placesRepositoryImpl: PlacesRepositoryImpl
) {
    suspend operator fun invoke(
        apiKey: String,
        query: String,
        languageCode: String?,
        locationRestriction: LocationRestriction?
    ): List<PlaceResponse> {
        return placesRepositoryImpl.getBetxiRestaurants(apiKey, query, languageCode, locationRestriction)
    }
}