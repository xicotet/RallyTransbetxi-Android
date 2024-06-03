package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.RestaurantsRepositoryImpl
import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(
    private val restaurantsRepositoryImpl: RestaurantsRepositoryImpl
) {
    suspend operator fun invoke() = restaurantsRepositoryImpl.getRestaurants()
}