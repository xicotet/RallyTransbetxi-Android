package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.data.sources.remote.StagesService
import javax.inject.Inject

interface StagesRepository {
    suspend fun getStages(): List<Stage>
}

class StagesRepositoryImpl @Inject constructor(
    private val stagesService: StagesService
) : StagesRepository {
    override suspend fun getStages(): List<Stage> {
        return stagesService.fetchStages()
    }
}