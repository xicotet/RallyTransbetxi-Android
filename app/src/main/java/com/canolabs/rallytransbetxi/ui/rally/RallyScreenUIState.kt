package com.canolabs.rallytransbetxi.ui.rally

import com.canolabs.rallytransbetxi.data.models.responses.Activity
import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import kotlinx.coroutines.flow.MutableStateFlow

data class RallyScreenUIState(
    val news: List<News> = emptyList(),
    val activities: List<Activity>  = emptyList(),
    val areBreakingNewsCollapsed: Boolean = false,
    val isShowAllBreakingNewsEnabled: Boolean = false,
    val areActivitiesCollapsed: Boolean = false,
    val isShowAllActivitiesEnabled: Boolean = false,
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<RallyScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<RallyScreenUIState>.setNews(news: List<News>) {
    value = value.copy(news = news)
}

fun MutableStateFlow<RallyScreenUIState>.setActivities(activities: List<Activity>) {
    value = value.copy(activities = activities)
}

fun MutableStateFlow<RallyScreenUIState>.setAreBreakingNewsCollapsed(areBreakingNewsCollapsed: Boolean) {
    value = value.copy(areBreakingNewsCollapsed = areBreakingNewsCollapsed)
}

fun MutableStateFlow<RallyScreenUIState>.setIsShowAllActivitiesEnabled(isShowAllActivitiesEnabled: Boolean) {
    value = value.copy(isShowAllActivitiesEnabled = isShowAllActivitiesEnabled)
}

fun MutableStateFlow<RallyScreenUIState>.setIsShowAllBreakingNewsEnabled(isShowBreakingNewsEnabled: Boolean) {
    value = value.copy(isShowAllBreakingNewsEnabled = isShowBreakingNewsEnabled)
}

fun MutableStateFlow<RallyScreenUIState>.setAreActivitiesCollapsed(areActivitiesCollapsed: Boolean) {
    value = value.copy(areActivitiesCollapsed = areActivitiesCollapsed)
}