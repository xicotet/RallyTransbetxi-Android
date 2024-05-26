package com.canolabs.rallytransbetxi.ui.rally

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.usecases.GetActivitiesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RallyScreenViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase
): ViewModel() {

    private var _state = MutableStateFlow(RallyScreenUIState())
    val state: StateFlow<RallyScreenUIState> = _state.asStateFlow()

    fun fetchNews() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setAreBreakingNewsCollapsed(true)

            val news = getNewsUseCase.invoke()
            val newsOrderedByDate = news.sortedByDescending { it.date }
            _state.setNews(newsOrderedByDate)

            _state.setIsLoading(false)
            _state.setAreBreakingNewsCollapsed(false)
        }
    }

    fun fetchActivities() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setAreActivitiesCollapsed(true)

            val activities = getActivitiesUseCase.invoke()
            val activitiesOrderedByIndex = activities.sortedBy {
                it.index
            }
            _state.setActivities(activitiesOrderedByIndex)

            _state.setAreActivitiesCollapsed(false)
            _state.setIsLoading(false)
        }
    }

    fun toggleBreakingNews() {
        _state.setAreBreakingNewsCollapsed(!_state.value.areBreakingNewsCollapsed)
    }

    fun toggleActivities() {
        _state.setAreActivitiesCollapsed(!_state.value.areActivitiesCollapsed)
    }

    fun toggleShowAllActivities() {
        _state.setIsShowAllActivitiesEnabled(!_state.value.isShowAllActivitiesEnabled)
    }
}