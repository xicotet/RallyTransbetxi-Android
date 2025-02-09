package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.RaceWarningsRepository

class GetGlobalRaceWarningUseCase(
    private val raceWarningsRepository: RaceWarningsRepository
) {
    suspend operator fun invoke() = raceWarningsRepository.getGlobalRaceWarning()
}