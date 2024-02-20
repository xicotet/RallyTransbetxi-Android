package com.canolabs.rallytransbetxi.ui.stages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StagesScreenViewModel : ViewModel() {
    private var _state = MutableStateFlow(StagesScreenUIState())
    val state: StateFlow<StagesScreenUIState> = _state.asStateFlow()

    fun fetchStages() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            //_state.setStages(GetStagesUseCase().invoke())
        }
    }
}