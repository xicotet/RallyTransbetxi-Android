package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.sources.remote.TeamsServiceImpl
import javax.inject.Inject

interface TeamsRepository {
    suspend fun getTeams(): List<Team>
}

class TeamsRepositoryImpl @Inject constructor(
    private val teamsServiceImpl: TeamsServiceImpl
) : TeamsRepository {
    override suspend fun getTeams(): List<Team> {
        return teamsServiceImpl.fetchTeams()
    }
}