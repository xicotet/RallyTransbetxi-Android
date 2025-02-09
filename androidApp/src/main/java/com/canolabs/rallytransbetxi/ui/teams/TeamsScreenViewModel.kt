package com.canolabs.rallytransbetxi.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetTeamsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class TeamsScreenViewModel : ViewModel() {
    private var _state = MutableStateFlow(TeamsScreenUIState())
    val state: StateFlow<TeamsScreenUIState> = _state.asStateFlow()

    private val getTeamsUseCase: GetTeamsUseCase by inject(GetTeamsUseCase::class.java)
    private val getGlobalResultsUseCase: GetGlobalResultsUseCase by inject(GetGlobalResultsUseCase::class.java)
    private val getStagesUseCase: GetStagesUseCase by inject(GetStagesUseCase::class.java)
    private val getStagesResultsUseCase: GetStagesResultsUseCase by inject(GetStagesResultsUseCase::class.java)

    fun fetchTeams() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setTeams(getTeamsUseCase.invoke())
            _state.setIsLoading(false)
        }
    }

    fun fetchGlobalResultOfATeam(team: Team) {
        viewModelScope.launch {
            _state.setIsLoadingCategoryResult(true)
            _state.setIsLoadingGlobalResult(true)
            _state.setIsLoadingGlobalTime(true)

            val globalResults = getGlobalResultsUseCase.invoke()
            
            // Get the Category position
            val filteredResultsByCategory = globalResults.filter { result ->
                team.category.let { selectedCategory ->
                    result.team.category.name == selectedCategory.name
                }
            }

            val sortedResultsByTimeCategory = filteredResultsByCategory.sortedBy { it.time }

            val categoryPosition = sortedResultsByTimeCategory.indexOfFirst { it.team.number == team.number }
            _state.setCategoryResult(categoryPosition + 1)
            _state.setIsLoadingCategoryResult(false)

            // Get the Global position
            val sortedResultsByTimeGlobal = globalResults.sortedBy { it.time }
            val globalPosition = sortedResultsByTimeGlobal.indexOfFirst { it.team.number == team.number }
            _state.setGlobalResult(globalPosition + 1)
            _state.setIsLoadingGlobalResult(false)

            // Get the Global time
            val globalTime = sortedResultsByTimeGlobal.firstOrNull { it.team.number == team.number }?.time ?: "00:00:00"
            _state.setGlobalTime(globalTime)
            _state.setIsLoadingGlobalTime(false)
        }
    }

    // Fetch the number of victories and best positions of a team in each stage
    fun fetchStageResultsOfATeam(team: Team) {
        viewModelScope.launch {
            _state.setIsLoadingStageVictories(true)
            _state.setIsLoadingBestStagePosition(true)

            // For every stage, get the ID of the stage
            val stageIds = getStagesUseCase.invoke().map { it.acronym }
            val stageResults = mutableListOf<Int>()
            for (stageId in stageIds) {
                val stageResultsByTeam = getStagesResultsUseCase.invoke(stageId)
                val stageResultsByCategory = stageResultsByTeam.filter {
                    it.team.category.name == team.category.name
                }
                val stageResultsByTime = stageResultsByCategory.sortedBy { it.time }
                val stagePosition = stageResultsByTime.indexOfFirst { it.team.number == team.number }
                if (stagePosition != -1) {
                    stageResults.add(stagePosition + 1)
                }
            }

            // Number of victories
            val stageVictories = stageResults.count { it == 1 }
            _state.setStageVictories(stageVictories)
            _state.setIsLoadingStageVictories(false)

            // Best stage position and the number of times it has been achieved
            val bestPosition = stageResults.minOrNull() ?: 0
            val numberOfTimesBestPosition = stageResults.count { it == bestPosition }
            _state.setBestStagePosition(bestPosition)
            _state.setNumberOfTimesBestPosition(numberOfTimesBestPosition)
            _state.setIsLoadingBestStagePosition(false)
        }
    }

    fun setIsSearchBarVisible(isSearchBarVisible: Boolean) {
        _state.setIsSearchBarVisible(isSearchBarVisible)
    }

    fun setSearchText(searchText: String) {
        _state.setSearchText(searchText)
    }

    fun removeSelectedRacingCategoryWithIndex(selectedRacingCategory: Int) {
        _state.removeSelectedRacingCategory(RacingCategory.entries[selectedRacingCategory])
    }

    fun addSelectedRacingCategoryWithIndex(selectedRacingCategory: Int) {
        _state.addSelectedRacingCategory(RacingCategory.entries[selectedRacingCategory])
    }
}