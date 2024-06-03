package com.canolabs.rallytransbetxi.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetTeamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsScreenViewModel @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase,
    private val getGlobalResultsUseCase: GetGlobalResultsUseCase
): ViewModel() {
    private var _state = MutableStateFlow(TeamsScreenUIState())
    val state: StateFlow<TeamsScreenUIState> = _state.asStateFlow()

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