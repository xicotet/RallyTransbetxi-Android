package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.repositories.TeamsRepository

class GetTeamsUseCase(
    private val teamsRepository: TeamsRepository
) {
    suspend operator fun invoke(): List<Team> {
        return teamsRepository.getTeams()
    }
}