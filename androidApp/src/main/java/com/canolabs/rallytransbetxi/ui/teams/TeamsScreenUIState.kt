package com.canolabs.rallytransbetxi.ui.teams

import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import kotlinx.coroutines.flow.MutableStateFlow

data class TeamsScreenUIState(
    val teams: List<Team> = emptyList(),
    val categoryResult: Int = 0,
    val globalResult: Int = 0,
    val globalTime: String = "",
    val stageVictories: Int = 0,
    val bestStagePosition: Int = 0,
    val numberOfTimesBestPosition: Int = 0,
    val isLoadingStageVictories: Boolean = false,
    val isLoadingBestStagePosition: Boolean = false,
    val isLoadingCategoryResult: Boolean = false,
    val isLoadingGlobalResult: Boolean = false,
    val isLoadingGlobalTime: Boolean = false,
    val isSearchBarVisible: Boolean = false,
    val searchText: String = "",
    val selectedRacingCategories: List<RacingCategory> = listOf(RacingCategory.PROTOTYPE),
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<TeamsScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<TeamsScreenUIState>.setTeams(teams: List<Team>) {
    value = value.copy(teams = teams)
}

fun MutableStateFlow<TeamsScreenUIState>.setIsSearchBarVisible(isSearchBarVisible: Boolean) {
    value = value.copy(isSearchBarVisible = isSearchBarVisible)
}

fun MutableStateFlow<TeamsScreenUIState>.setCategoryResult(globalResults: Int) {
    value = value.copy(categoryResult = globalResults)
}

fun MutableStateFlow<TeamsScreenUIState>.setIsLoadingCategoryResult(isLoadingGlobalResult: Boolean) {
    value = value.copy(isLoadingCategoryResult = isLoadingGlobalResult)
}

fun MutableStateFlow<TeamsScreenUIState>.setStageVictories(stageVictories: Int) {
    value = value.copy(stageVictories = stageVictories)
}

fun MutableStateFlow<TeamsScreenUIState>.setIsLoadingStageVictories(isLoadingStageVictories: Boolean) {
    value = value.copy(isLoadingStageVictories = isLoadingStageVictories)
}

fun MutableStateFlow<TeamsScreenUIState>.setBestStagePosition(bestStagePosition: Int) {
    value = value.copy(bestStagePosition = bestStagePosition)
}

fun MutableStateFlow<TeamsScreenUIState>.setIsLoadingBestStagePosition(isLoadingBestStagePosition: Boolean) {
    value = value.copy(isLoadingBestStagePosition = isLoadingBestStagePosition)
}

fun MutableStateFlow<TeamsScreenUIState>.setNumberOfTimesBestPosition(numberOfTimesBestPosition: Int) {
    value = value.copy(numberOfTimesBestPosition = numberOfTimesBestPosition)
}

fun MutableStateFlow<TeamsScreenUIState>.setGlobalResult(globalResult: Int) {
    value = value.copy(globalResult = globalResult)
}

fun MutableStateFlow<TeamsScreenUIState>.setIsLoadingGlobalResult(isLoadingGlobalResult: Boolean) {
    value = value.copy(isLoadingGlobalResult = isLoadingGlobalResult)
}

fun MutableStateFlow<TeamsScreenUIState>.setGlobalTime(globalTime: String) {
    value = value.copy(globalTime = globalTime)
}

fun MutableStateFlow<TeamsScreenUIState>.setIsLoadingGlobalTime(isLoadingGlobalTime: Boolean) {
    value = value.copy(isLoadingGlobalTime = isLoadingGlobalTime)
}

fun MutableStateFlow<TeamsScreenUIState>.setSearchText(searchText: String) {
    value = value.copy(searchText = searchText)
}

fun MutableStateFlow<TeamsScreenUIState>.addSelectedRacingCategory(selectedRacingCategory: RacingCategory) {
    value = value.copy(selectedRacingCategories = value.selectedRacingCategories + selectedRacingCategory)
}

fun MutableStateFlow<TeamsScreenUIState>.removeSelectedRacingCategory(selectedRacingCategory: RacingCategory) {
    value = value.copy(selectedRacingCategories = value.selectedRacingCategories - selectedRacingCategory)
}