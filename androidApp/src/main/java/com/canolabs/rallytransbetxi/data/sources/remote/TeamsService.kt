package com.canolabs.rallytransbetxi.data.sources.remote

import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import com.canolabs.rallytransbetxi.data.models.responses.Category
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.repositories.CategoriesRepositoryImpl
import dev.gitlive.firebase.firestore.DocumentReference

interface TeamsService {
    suspend fun fetchTeams(): List<Team>
}

class TeamsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val categoriesRepositoryImpl: CategoriesRepositoryImpl // TODO: Refactor this to avoid injecting a repository...
) : TeamsService {

    override suspend fun fetchTeams(): List<Team> {
        return try {
            val categories = categoriesRepositoryImpl.getCategories().associateBy { it.categoryId }
            val querySnapshot = firebaseFirestore.collection("teams").get()

            querySnapshot.documents.mapNotNull { document ->
                runCatching {
                    fetchTeam(document, categories)
                }.getOrNull()
            }
        } catch (e: FirebaseFirestoreException) {
            println("Error fetching teams: ${e.message}")
            emptyList()
        }
    }

    private fun fetchTeam(document: DocumentSnapshot, categories: Map<String, Category>): Team {
        val team = document.data<Team>()
        val categoryRef = document.get<DocumentReference>("categoryReference")
        val categoryId = categoryRef.id // Extracting only the last part of the path
        team.category = categories[categoryId] ?: return team
        return team
    }
}