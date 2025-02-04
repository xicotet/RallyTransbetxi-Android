package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.data.repositories.NewsRepositoryImpl
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepositoryImpl: NewsRepositoryImpl
) {
    suspend operator fun invoke(): List<News> {
        return newsRepositoryImpl.getNews()
    }
}