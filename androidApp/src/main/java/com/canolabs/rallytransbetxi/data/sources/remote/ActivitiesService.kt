package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Activity
import dev.gitlive.firebase.firestore.FirebaseFirestore

interface ActivitiesService {
    suspend fun fetchActivities(): List<Activity>
}

class ActivitiesServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : ActivitiesService {
    override suspend fun fetchActivities(): List<Activity> {
        return try {
            val querySnapshot = firebaseFirestore.collection("activities").get()
            querySnapshot.documents.map { document ->
                document.data<Activity>().copy(index = document.id.toInt())
            }
        } catch (e: Exception) {
            println("Error fetching activities: ${e.message}")
            emptyList()
        }
    }
}