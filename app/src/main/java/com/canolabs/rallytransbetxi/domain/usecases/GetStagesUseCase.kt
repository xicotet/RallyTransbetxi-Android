package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.data.repositories.StagesRepository
import javax.inject.Inject

class GetStagesUseCase @Inject constructor(
    private val stagesRepository: StagesRepository
) {
    suspend operator fun invoke(): List<Stage> {
        return stagesRepository.getStages()
    }
}