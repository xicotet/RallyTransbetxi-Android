package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import dev.gitlive.firebase.firestore.FirebaseFirestore

interface RestaurantsService {
    suspend fun fetchRestaurants(): List<Restaurant>
}

class RestaurantsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : RestaurantsService {

    override suspend fun fetchRestaurants(): List<Restaurant> {
        return try {
            firebaseFirestore.collection("restaurants").get()
                .documents.map { it.data<Restaurant>() }
        } catch (e: Exception) {
            println("Error fetching restaurants: ${e.message}")
            emptyList()
        }
    }
}