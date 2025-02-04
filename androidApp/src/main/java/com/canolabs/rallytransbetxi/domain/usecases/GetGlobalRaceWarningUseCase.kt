package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.RaceWarningsRepositoryImpl
import javax.inject.Inject

class GetGlobalRaceWarningUseCase @Inject constructor(
    private val raceWarningsRepository: RaceWarningsRepositoryImpl
) {
    suspend operator fun invoke() = raceWarningsRepository.getGlobalRaceWarning()
}