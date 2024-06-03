package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import com.canolabs.rallytransbetxi.data.sources.remote.RestaurantsServiceImpl
import javax.inject.Inject

interface RestaurantsRepository {
    suspend fun getRestaurants(): List<Restaurant>
}

class RestaurantsRepositoryImpl @Inject constructor(
    private val restaurantsServiceImpl: RestaurantsServiceImpl,
): RestaurantsRepository {
    override suspend fun getRestaurants(): List<Restaurant> {
        return restaurantsServiceImpl.fetchRestaurants()
    }
}