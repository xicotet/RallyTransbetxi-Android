package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Activity
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface ActivitiesService {
    suspend fun fetchActivities(): List<Activity>
}

class ActivitiesServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : ActivitiesService {
    override suspend fun fetchActivities(): List<Activity> {
        return try {
            val activities = mutableListOf<Activity>()
            val querySnapshot = firebaseFirestore.collection("activities").get().await()
            for (document in querySnapshot.documents) {
                val activity = document.toObject(Activity::class.java)
                activity?.let {
                    it.index = document.id.toInt()
                    activities.add(it)
                }
            }
            activities
        } catch (e: Exception) {
            Log.e("ActivitiesServiceImpl", "Error fetching activities: ${e.message}")
            emptyList()
        }
    }
}