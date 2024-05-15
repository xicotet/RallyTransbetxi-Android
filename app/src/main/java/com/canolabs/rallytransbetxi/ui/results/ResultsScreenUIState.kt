package com.canolabs.rallytransbetxi.ui.results

import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import kotlinx.coroutines.flow.MutableStateFlow

data class ResultsScreenUIState(
    val globalResults: List<Result> = emptyList(),
    val stageResults: List<Result> = emptyList(),
    val stages: List<Stage> = emptyList(),
    val stageSelected: Stage = Stage(),
    val isSearchBarVisible: Boolean = false,
    val isBottomSheetSearchBarVisible: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val isBottomSheetLoading: Boolean = false,
    val searchText: String = "",
    val selectedTabIndex: Int = 0,
    val selectedRacingCategories: List<RacingCategory> = listOf(RacingCategory.PROTOTYPE),
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
): UIState

fun MutableStateFlow<ResultsScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<ResultsScreenUIState>.setGlobalResults(results: List<Result>) {
    value = value.copy(globalResults = results)
}

fun MutableStateFlow<ResultsScreenUIState>.setStages(stages: List<Stage>) {
    value = value.copy(stages = stages)
}

fun MutableStateFlow<ResultsScreenUIState>.setSelectedTabIndex(selectedTabIndex: Int) {
    value = value.copy(selectedTabIndex = selectedTabIndex)
}

fun MutableStateFlow<ResultsScreenUIState>.addSelectedRacingCategory(selectedRacingCategory: RacingCategory) {
    value = value.copy(selectedRacingCategories = value.selectedRacingCategories + selectedRacingCategory)
}

fun MutableStateFlow<ResultsScreenUIState>.removeSelectedRacingCategory(selectedRacingCategory: RacingCategory) {
    value = value.copy(selectedRacingCategories = value.selectedRacingCategories - selectedRacingCategory)
}

fun MutableStateFlow<ResultsScreenUIState>.setSearchText(searchText: String) {
    value = value.copy(searchText = searchText)
}

fun MutableStateFlow<ResultsScreenUIState>.setIsSearchBarVisible(isSearchBarVisible: Boolean) {
    value = value.copy(isSearchBarVisible = isSearchBarVisible)
}

fun MutableStateFlow<ResultsScreenUIState>.setIsBottomSheetSearchBarVisible(isBottomSheetSearchBarVisible: Boolean) {
    value = value.copy(isBottomSheetSearchBarVisible = isBottomSheetSearchBarVisible)
}

fun MutableStateFlow<ResultsScreenUIState>.setIsBottomSheetVisible(isBottomSheetVisible: Boolean) {
    value = value.copy(isBottomSheetVisible = isBottomSheetVisible)
}

fun MutableStateFlow<ResultsScreenUIState>.setStageResults(stageResults: List<Result>) {
    value = value.copy(stageResults = stageResults)
}

fun MutableStateFlow<ResultsScreenUIState>.setSelectedStage(stageSelected: Stage) {
    value = value.copy(stageSelected = stageSelected)
}

fun MutableStateFlow<ResultsScreenUIState>.setIsBottomSheetLoading(isBottomSheetLoading: Boolean) {
    value = value.copy(isBottomSheetLoading = isBottomSheetLoading)
}