package com.canolabs.rallytransbetxi.ui.teams

import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import kotlinx.coroutines.flow.MutableStateFlow

data class TeamsScreenUIState(
    val teams: List<Team> = emptyList(),
    val categoryResult: Int = 0,
    val isLoadingCategoryResult: Boolean = false,
    val globalResult: Int = 0,
    val isLoaadingGlobalResult: Boolean = false,
    val globalTime: String = "",
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

fun MutableStateFlow<TeamsScreenUIState>.setGlobalResult(globalResult: Int) {
    value = value.copy(globalResult = globalResult)
}

fun MutableStateFlow<TeamsScreenUIState>.setIsLoadingGlobalResult(isLoadingGlobalResult: Boolean) {
    value = value.copy(isLoaadingGlobalResult = isLoadingGlobalResult)
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