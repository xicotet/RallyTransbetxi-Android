package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Category
import dev.gitlive.firebase.firestore.FirebaseFirestore

interface CategoriesService {
    suspend fun fetchCategories(): List<Category>
}

class CategoriesServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : CategoriesService {
    override suspend fun fetchCategories(): List<Category> {
        return try {
            // TODO: Move the collectionPath to a constant or build config for security
            val querySnapshot = firebaseFirestore.collection("categories").get()
            querySnapshot.documents.map { document ->
                document.data<Category>().copy(categoryId = document.id)
            }
        } catch (e: Exception) {
            println("Error fetching categories: ${e.message}")
            emptyList()
        }
    }
}