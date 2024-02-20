package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.data.sources.remote.StagesDataSource

interface StagesRepository {
    suspend fun getStages(): List<Stage>
}

class StagesRepositoryImpl(private val stagesDataSource: StagesDataSource) : StagesRepository {
    override suspend fun getStages(): List<Stage> {
        return stagesDataSource.fetchStages()
    }
}