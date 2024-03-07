package com.canolabs.rallytransbetxi.ui.results

import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import kotlinx.coroutines.flow.MutableStateFlow

data class ResultsScreenUIState(
    val results: List<Result> = emptyList(),
    val selectedTabIndex: Int = 0,
    val selectedRacingCategory: RacingCategory = RacingCategory.PROTOTYPE,
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
): UIState

fun MutableStateFlow<ResultsScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<ResultsScreenUIState>.setResults(results: List<Result>) {
    value = value.copy(results = results)
}

fun MutableStateFlow<ResultsScreenUIState>.setSelectedTabIndex(selectedTabIndex: Int) {
    value = value.copy(selectedTabIndex = selectedTabIndex)
}

fun MutableStateFlow<ResultsScreenUIState>.setSelectedRacingCategory(selectedRacingCategory: Int) {
    value = value.copy(selectedRacingCategory = RacingCategory.entries[selectedRacingCategory])
}


