package com.canolabs.rallytransbetxi.ui.results

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalRaceWarningUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStageRaceWarningUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.util.Locale

class ResultsScreenViewModel : ViewModel() {
    private var _state = MutableStateFlow(ResultsScreenUIState())
    val state: StateFlow<ResultsScreenUIState> = _state.asStateFlow()

    private val getGlobalResultsUseCase: GetGlobalResultsUseCase by inject(GetGlobalResultsUseCase::class.java)
    private val getStagesResultsUseCase: GetStagesResultsUseCase by inject(GetStagesResultsUseCase::class.java)
    private val getGlobalRaceWarningUseCase: GetGlobalRaceWarningUseCase by inject(GetGlobalRaceWarningUseCase::class.java)
    private val getStageRaceWarningUseCase: GetStageRaceWarningUseCase by inject(GetStageRaceWarningUseCase::class.java)
    private val getStagesUseCase: GetStagesUseCase by inject(GetStagesUseCase::class.java)

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

    fun fetchGlobalRaceWarning() {
        viewModelScope.launch {
            _state.setRaceWarning(null)
            _state.setRaceWarning(getGlobalRaceWarningUseCase.invoke())
        }
    }

    fun fetchStageRaceWarning(stageId: String) {
        viewModelScope.launch {
            _state.setRaceWarning(null)
            _state.setRaceWarning(getStageRaceWarningUseCase.invoke(stageId))
        }
    }

    fun fetchStages() {
        viewModelScope.launch {
            _state.setStages(getStagesUseCase.invoke())
        }
    }

    fun fetchLanguage(sharedPreferences: SharedPreferences) {
        viewModelScope.launch {
            val language = sharedPreferences.getString("SelectedLanguage", Locale.getDefault().language)
            if (language != null) {
                _state.setLanguage(Language.entries.find { it.getLanguageCode() == language }!!)
            }
        }
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