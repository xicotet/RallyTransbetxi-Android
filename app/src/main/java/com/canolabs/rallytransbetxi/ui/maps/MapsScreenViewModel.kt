package com.canolabs.rallytransbetxi.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.usecases.GetStageByAcronymUseCase
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsScreenViewModel @Inject constructor(
    private val getStageByAcronymUseCase: GetStageByAcronymUseCase
): ViewModel() {
    private var _state = MutableStateFlow(MapsScreenUIState())
    val state: StateFlow<MapsScreenUIState> = _state.asStateFlow()

    fun fetchStage(acronym: String) {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setStage(getStageByAcronymUseCase(acronym))
            _state.setIsLoading(false)
        }
    }

    fun setMapProperties(mapProperties: MapProperties) {
        _state.setMapProperties(mapProperties)
    }

    fun setUiSettings(uiSettings: MapUiSettings) {
        _state.setUiSettings(uiSettings)
    }
}