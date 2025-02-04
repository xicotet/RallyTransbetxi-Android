package com.canolabs.rallytransbetxi.domain.usecases

import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.repositories.TeamsRepositoryImpl
import javax.inject.Inject

class GetTeamsUseCase @Inject constructor(
    private val teamsRepositoryImpl: TeamsRepositoryImpl
) {
    suspend operator fun invoke(): List<Team> {
        return teamsRepositoryImpl.getTeams()
    }
}