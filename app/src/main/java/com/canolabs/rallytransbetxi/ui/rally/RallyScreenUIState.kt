package com.canolabs.rallytransbetxi.ui.rally

import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import kotlinx.coroutines.flow.MutableStateFlow

data class RallyScreenUIState(
    val news: List<News> = emptyList(),
    val areBreakingNewsCollapsed: Boolean = false,
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<RallyScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<RallyScreenUIState>.setNews(news: List<News>) {
    value = value.copy(news = news)
}

fun MutableStateFlow<RallyScreenUIState>.setAreBreakingNewsCollapsed(areBreakingNewsCollapsed: Boolean) {
    value = value.copy(areBreakingNewsCollapsed = areBreakingNewsCollapsed)
}