package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.data.repositories.StagesRepositoryImpl
import javax.inject.Inject

class GetStagesUseCase @Inject constructor(
    private val stagesRepositoryImpl: StagesRepositoryImpl
) {
    suspend operator fun invoke(): List<Stage> {
        return stagesRepositoryImpl.getStages()
    }
}