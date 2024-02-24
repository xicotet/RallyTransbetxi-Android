package com.canolabs.rallytransbetxi.data.repositories

import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.data.sources.remote.CategoriesServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.TeamsServiceImpl

interface TeamsRepository {
    suspend fun getTeams(): List<Team>
}

class TeamsRepositoryImpl(
    private val teamsServiceImpl: TeamsServiceImpl,
    private val categoriesServiceImpl: CategoriesServiceImpl
) : TeamsRepository {
    override suspend fun getTeams(): List<Team> {
        val teams = teamsServiceImpl.fetchTeams()
        teams.forEach { team ->
            team.category = categoriesServiceImpl.fetchCategoryOfATeam(team.number)
        }
        return teams
    }
}