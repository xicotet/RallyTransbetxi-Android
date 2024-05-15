package com.canolabs.rallytransbetxi.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesResultsUseCase
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
    private val getStagesResultsUseCase: GetStagesResultsUseCase,
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

    fun fetchStagesResults(stageAcronym: String) {
        viewModelScope.launch {
            _state.setIsBottomSheetLoading(true)
            _state.setStageResults(getStagesResultsUseCase.invoke(stageAcronym))
            _state.setIsBottomSheetLoading(false)
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

    fun removeSelectedRacingCategoryWithIndex(selectedRacingCategory: Int) {
        _state.removeSelectedRacingCategory(RacingCategory.entries[selectedRacingCategory])
    }

    fun addSelectedRacingCategoryWithIndex(selectedRacingCategory: Int) {
        _state.addSelectedRacingCategory(RacingCategory.entries[selectedRacingCategory])
    }

    fun setSearchText(searchText: String) {
        _state.setSearchText(searchText)
    }

    fun setIsSearchBarVisible(isSearchBarVisible: Boolean) {
        _state.setIsSearchBarVisible(isSearchBarVisible)
    }

    fun setIsBottomSheetSearchBarVisible(isBottomSheetSearchBarVisible: Boolean) {
        _state.setIsBottomSheetSearchBarVisible(isBottomSheetSearchBarVisible)
    }

    fun setIsBottomSheetVisible(isBottomSheetVisible: Boolean) {
        _state.setIsBottomSheetVisible(isBottomSheetVisible)
    }

    fun setSelectedStage(stageSelected: Stage) {
        _state.setSelectedStage(stageSelected)
    }
}