package com.canolabs.rallytransbetxi.data.sources.remote

import com.canolabs.rallytransbetxi.data.models.responses.Stage

interface StagesDataSource {
    suspend fun fetchStages(): List<Stage>
}

class StagesDataSourceImpl : StagesDataSource {
    override suspend fun fetchStages(): List<Stage> {
        return emptyList()
    }
}