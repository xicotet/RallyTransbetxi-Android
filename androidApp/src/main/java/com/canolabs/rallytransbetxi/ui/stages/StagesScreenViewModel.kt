package com.canolabs.rallytransbetxi.ui.stages

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StagesScreenViewModel @Inject constructor(
    private val getStagesUseCase: GetStagesUseCase,
): ViewModel() {
    private var _state = MutableStateFlow(StagesScreenUIState())
    val state: StateFlow<StagesScreenUIState> = _state.asStateFlow()

    fun fetchStages() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setStages(getStagesUseCase.invoke())
            _state.setIsLoading(false)
        }
    }

    fun setIsSearchBarVisible(isSearchBarVisible: Boolean) {
        _state.setIsSearchBarVisible(isSearchBarVisible)
    }

    fun setSearchText(searchText: String) {
        _state.setSearchText(searchText)
    }

    fun fetchLanguage(sharedPreferences: SharedPreferences) {
        viewModelScope.launch {
            val language = sharedPreferences.getString("SelectedLanguage", Locale.getDefault().language)
            if (language != null) {
                _state.setLanguage(Language.entries.find { it.getLanguageCode() == language }!!)
            }
        }
    }
}