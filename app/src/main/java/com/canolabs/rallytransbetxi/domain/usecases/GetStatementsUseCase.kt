package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.StatementsRepositoryImpl
import javax.inject.Inject

class GetStatementsUseCase @Inject constructor(
    private val statementsRepositoryImpl: StatementsRepositoryImpl
) {
    suspend operator fun invoke() = statementsRepositoryImpl.getStatements()
}