package com.canolabs.rallytransbetxi.ui.stages

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import kotlinx.coroutines.flow.MutableStateFlow

data class StagesScreenUIState(
    val stages: List<Stage> = emptyList(),
    val language: Language? = null,
    val isSearchBarVisible: Boolean = false,
    val searchText: String = "",
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<StagesScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<StagesScreenUIState>.setStages(stages: List<Stage>) {
    value = value.copy(stages = stages)
}

fun MutableStateFlow<StagesScreenUIState>.setLanguage(language: Language) {
    value = value.copy(language = language)
}

fun MutableStateFlow<StagesScreenUIState>.setIsSearchBarVisible(isSearchBarVisible: Boolean) {
    value = value.copy(isSearchBarVisible = isSearchBarVisible)
}

fun MutableStateFlow<StagesScreenUIState>.setSearchText(searchText: String) {
    value = value.copy(searchText = searchText)
}