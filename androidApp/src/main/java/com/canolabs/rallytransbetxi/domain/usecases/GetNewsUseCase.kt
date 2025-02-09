package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.data.repositories.NewsRepository

class GetNewsUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): List<News> {
        return newsRepository.getNews()
    }
}