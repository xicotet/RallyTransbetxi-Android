package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Category
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.repositories.CategoriesRepositoryImpl
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

interface TeamsService {
    suspend fun fetchTeams(): List<Team>
}

class TeamsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val categoriesRepositoryImpl: CategoriesRepositoryImpl,
) : TeamsService {
    override suspend fun fetchTeams(): List<Team> {
        val startTime = System.currentTimeMillis()

        // Fetch all categories and store them in a map
        val categories = categoriesRepositoryImpl.getCategories().associateBy { it.categoryId }

        val teams = mutableListOf<Team>()

        try {
            val querySnapshot = firebaseFirestore.collection("teams").get().await()

            for (document in querySnapshot.documents) {
                try {
                    val team = fetchTeam(document, categories)
                    teams.add(team)
                } catch (e: Exception) {
                    Log.d("TeamsServiceImpl", "Error getting team ${document.id}: ", e)
                }
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("TeamsServiceImpl", "Error fetching teams: ${e.message}", e)
            // Handle permission denied or other Firestore exceptions
            if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                Log.e("TeamsServiceImpl", "Permission denied for fetching teams.")
                // Provide feedback to the user or attempt a recovery action
            }
        }

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
        Log.d("TeamsServiceImpl", "Elapsed time for fetchTeams request: $elapsedTime ms")
        return teams
    }

    private fun fetchTeam(
        document: DocumentSnapshot,
        categories: Map<String, Category>
    ): Team {
        val team = document.toObject(Team::class.java) ?: return Team()
        val categoryReference = document.get("categoryReference") as DocumentReference
        team.category = categories[categoryReference.id]!!
        return team
    }
}