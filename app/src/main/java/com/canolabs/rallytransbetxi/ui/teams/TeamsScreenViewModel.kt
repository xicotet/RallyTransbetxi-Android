package com.canolabs.rallytransbetxi.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.usecases.GetTeamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsScreenViewModel @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase
): ViewModel() {
    private var _state = MutableStateFlow(TeamsScreenUIState())
    val state: StateFlow<TeamsScreenUIState> = _state.asStateFlow()

    fun fetchTeams() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setTeams(getTeamsUseCase.invoke())
            _state.setIsLoading(false)
        }
    }
}