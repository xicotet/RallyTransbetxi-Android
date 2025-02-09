package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.RestaurantsRepository

class GetRestaurantsUseCase(
    private val restaurantsRepository: RestaurantsRepository
) {
    suspend operator fun invoke() = restaurantsRepository.getRestaurants()
}