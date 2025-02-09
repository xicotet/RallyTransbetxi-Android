package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RestaurantsService {
    suspend fun fetchRestaurants(): List<Restaurant>
}

class RestaurantsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : RestaurantsService {
    override suspend fun fetchRestaurants(): List<Restaurant> {
        return try {
            val restaurants = mutableListOf<Restaurant>()
            val querySnapshot = firebaseFirestore.collection("restaurants").get().await()
            for (document in querySnapshot.documents) {
                Log.d("RestaurantsServiceImpl", "Document: ${document.data}")
                val restaurant = document.toObject(Restaurant::class.java)
                restaurant?.let {
                    restaurants.add(it)
                }
            }
            restaurants
        } catch (e: Exception) {
            Log.d("RestaurantsServiceImpl", "Error fetching restaurants: ${e.message}")
            emptyList()
        }
    }
}