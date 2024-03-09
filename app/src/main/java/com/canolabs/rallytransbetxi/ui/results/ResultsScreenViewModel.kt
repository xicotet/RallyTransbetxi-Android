package com.canolabs.rallytransbetxi.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsScreenViewModel @Inject constructor(
    private val getGlobalResultsUseCase: GetGlobalResultsUseCase,
    private val getStagesUseCase: GetStagesUseCase
): ViewModel() {
    private var _state = MutableStateFlow(ResultsScreenUIState())
    val state: StateFlow<ResultsScreenUIState> = _state.asStateFlow()

    fun fetchGlobalResults() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setGlobalResults(getGlobalResultsUseCase.invoke())
            _state.setIsLoading(false)
        }
    }

    fun fetchStages() {
        viewModelScope.launch {
            _state.setStages(getStagesUseCase.invoke())
        }
    }

    fun setSelectedTabIndex(selectedTabIndex: Int) {
        _state.setSelectedTabIndex(selectedTabIndex)
    }

    fun setSelectedRacingCategory(selectedRacingCategory: Int) {
        _state.setSelectedRacingCategory(selectedRacingCategory)
    }

    fun setSearchText(searchText: String) {
        _state.setSearchText(searchText)
    }

    fun setIsSearchBarVisible(isSearchBarVisible: Boolean) {
        _state.setIsSearchBarVisible(isSearchBarVisible)
    }
}