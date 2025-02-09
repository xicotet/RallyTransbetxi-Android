package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.data.repositories.StagesRepository

class GetStageByAcronymUseCase(
    private val stagesRepository: StagesRepository
) {
    suspend operator fun invoke(acronym: String): Stage {
        return stagesRepository.getStageByAcronym(acronym)
    }
}