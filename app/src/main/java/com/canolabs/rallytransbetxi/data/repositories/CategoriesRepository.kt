package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Category
import com.canolabs.rallytransbetxi.data.sources.remote.CategoriesServiceImpl
import javax.inject.Inject


interface CategoriesRepository {
    suspend fun getCategories(): List<Category>
}

class CategoriesRepositoryImpl @Inject constructor(
    private val categoriesServiceImpl: CategoriesServiceImpl
) : CategoriesRepository {
    override suspend fun getCategories(): List<Category> {
        return categoriesServiceImpl.fetchCategories()
    }
}