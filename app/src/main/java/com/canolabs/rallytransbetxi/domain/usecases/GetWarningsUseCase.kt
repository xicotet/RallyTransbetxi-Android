package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.WarningsRepositoryImpl
import javax.inject.Inject

class GetWarningsUseCase @Inject constructor(
    private val warningsRepositoryImpl: WarningsRepositoryImpl
) {
    suspend operator fun invoke() = warningsRepositoryImpl.getWarnings()
}