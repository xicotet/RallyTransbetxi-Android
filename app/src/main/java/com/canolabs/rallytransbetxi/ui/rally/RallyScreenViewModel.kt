package com.canolabs.rallytransbetxi.ui.rally

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.usecases.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RallyScreenViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
): ViewModel() {

    private var _state = MutableStateFlow(RallyScreenUIState())
    val state: StateFlow<RallyScreenUIState> = _state.asStateFlow()

    fun fetchNews() {
        viewModelScope.launch {
            _state.setIsLoading(true)
            val news = getNewsUseCase.invoke()
            val newsOrderedByDate = news.sortedByDescending { it.date }
            _state.setNews(newsOrderedByDate)
            _state.setIsLoading(false)
        }
    }

    fun toggleBreakingNews() {
        _state.setAreBreakingNewsCollapsed(!_state.value.areBreakingNewsCollapsed)
    }
}