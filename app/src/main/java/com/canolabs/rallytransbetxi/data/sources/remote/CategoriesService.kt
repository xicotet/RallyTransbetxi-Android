package com.canolabs.rallytransbetxi.data.sources.remote

import android.util.Log
import com.canolabs.rallytransbetxi.data.models.responses.Category
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CategoriesService {
    suspend fun fetchCategories(): List<Category>
    suspend fun fetchCategoryOfATeam(teamId: String): Category
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

    override suspend fun fetchCategoryOfATeam(teamId: String): Category {
        lateinit var category: Category
        try {
            val querySnapshot = firebaseFirestore.collection("teams").document(teamId).get().await()
            querySnapshot.data?.let {
                val documentReference = it["category"] as DocumentReference
                documentReference.get()
                    .addOnSuccessListener { documentSnapshot ->
                        category = if (documentSnapshot != null) {
                            Category(
                                categoryId = documentSnapshot.id,
                                name = documentSnapshot["name"] as String
                            )
                        } else Category()
                    }
                    .addOnFailureListener { exception ->
                        Log.d("CategoriesServiceImpl", "Error getting category of a team: ", exception)
                    }
            }
            return category
        } catch (e: Exception) {
            return Category()
        }
    }
}