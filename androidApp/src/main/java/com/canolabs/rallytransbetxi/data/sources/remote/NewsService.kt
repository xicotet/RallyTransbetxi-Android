package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.News
import dev.gitlive.firebase.firestore.FirebaseFirestore


interface NewsService {
    suspend fun fetchNews(): List<News>
}

class NewsServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : NewsService {
    override suspend fun fetchNews(): List<News> {
        return try {
            val querySnapshot = firebaseFirestore.collection("news").get()
            querySnapshot.documents.map { document ->
                document.data<News>()
            }
        } catch (e: Exception) {
            println("Error fetching news: ${e.message}")
            emptyList()
        }
    }
}