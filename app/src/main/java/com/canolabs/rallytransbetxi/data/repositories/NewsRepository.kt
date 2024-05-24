package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.data.sources.remote.NewsServiceImpl
import javax.inject.Inject

interface NewsRepository {
    suspend fun getNews(): List<News>
}

class NewsRepositoryImpl @Inject constructor(
    private val newsServiceImpl: NewsServiceImpl
) : NewsRepository {
    override suspend fun getNews(): List<News> {
        return newsServiceImpl.fetchNews()
    }
}