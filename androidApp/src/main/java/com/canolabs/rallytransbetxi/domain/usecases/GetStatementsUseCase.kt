package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.repositories.StatementsRepository

class GetStatementsUseCase(
    private val statementsRepository: StatementsRepository
) {
    suspend operator fun invoke() = statementsRepository.getStatements()
}