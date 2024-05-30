package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CategoriesService {
    suspend fun fetchCategories(): List<Category>
}

class CategoriesServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : CategoriesService {
    override suspend fun fetchCategories(): List<Category> {
        return try {
            val categoriesList = mutableListOf<Category>()
            val querySnapshot = firebaseFirestore.collection("categories").get().await()
            for (document in querySnapshot.documents) {
                val category = document.toObject(Category::class.java)
                    ?.copy(categoryId = document.id)
                category?.let {
                    categoriesList.add(it)
                }
            }
            categoriesList
        } catch (e: Exception) {
            // Handle error
            emptyList()
        }
    }
}