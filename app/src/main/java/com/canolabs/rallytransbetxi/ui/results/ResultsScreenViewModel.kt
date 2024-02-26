package com.canolabs.rallytransbetxi.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsScreenViewModel @Inject constructor(
    private val getGlobalResultsUseCase: GetGlobalResultsUseCase
): ViewModel() {
    private var _state = MutableStateFlow(ResultsScreenUIState())
    val state: StateFlow<ResultsScreenUIState> = _state.asStateFlow()

    fun fetchGlobalResults() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setResults(getGlobalResultsUseCase.invoke())
            _state.setIsLoading(false)
        }
    }

    fun setSelectedTabIndex(selectedTabIndex: Int) {
        _state.setSelectedTabIndex(selectedTabIndex)
    }

    fun setSelectedRacingCategory(selectedRacingCategory: Int) {
        _state.setSelectedRacingCategory(selectedRacingCategory)
    }
}