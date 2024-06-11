package com.canolabs.rallytransbetxi.ui.rally

import com.canolabs.rallytransbetxi.data.models.responses.Activity
import com.canolabs.rallytransbetxi.data.models.responses.HallOfFame
import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import com.canolabs.rallytransbetxi.data.models.responses.Warning
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import com.canolabs.rallytransbetxi.domain.entities.FontSizeFactor
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.domain.entities.Theme
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import kotlinx.coroutines.flow.MutableStateFlow

data class RallyScreenUIState(
    val news: List<News> = emptyList(),
    val activities: List<Activity>  = emptyList(),
    val hallOfFame: List<HallOfFame> = emptyList(),
    val restaurants: List<Restaurant> = emptyList(),
    val warnings: List<Warning> = emptyList(),
    val isHallOfFameLoading: Boolean = false,
    val language: Language? = null,
    val theme: Theme? = null,
    val directionsProfile: DirectionsProfile? = null,
    val fontSizeFactor: FontSizeFactor? = null,
    val isDialogShowing: Boolean = false,
    val warningShownOnDialog: Warning? = null,
    val areWarningsCollapsed: Boolean = true,
    val isShowAllWarningsEnabled: Boolean = false,
    val areBreakingNewsCollapsed: Boolean = false,
    val isShowAllBreakingNewsEnabled: Boolean = false,
    val areActivitiesCollapsed: Boolean = false,
    val isShowAllActivitiesEnabled: Boolean = false,
    val isSettingsBottomSheetVisible: Boolean = false,
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

fun MutableStateFlow<RallyScreenUIState>.setDirectionsProfile(directionsProfile: DirectionsProfile) {
    value = value.copy(directionsProfile = directionsProfile)
}

fun MutableStateFlow<RallyScreenUIState>.setLanguage(language: Language) {
    value = value.copy(language = language)
}

fun MutableStateFlow<RallyScreenUIState>.setIsDialogShowing(isDialogShowing: Boolean) {
    value = value.copy(isDialogShowing = isDialogShowing)
}

fun MutableStateFlow<RallyScreenUIState>.setWarningShownOnDialog(warning: Warning) {
    value = value.copy(warningShownOnDialog = warning)
}

fun MutableStateFlow<RallyScreenUIState>.setRestaurants(restaurants: List<Restaurant>) {
    value = value.copy(restaurants = restaurants)
}

fun MutableStateFlow<RallyScreenUIState>.setWarnings(warnings: List<Warning>) {
    value = value.copy(warnings = warnings)
}

fun MutableStateFlow<RallyScreenUIState>.setAreWarningsCollapsed(areWarningsCollapsed: Boolean) {
    value = value.copy(areWarningsCollapsed = areWarningsCollapsed)
}

fun MutableStateFlow<RallyScreenUIState>.setIsShowAllWarningsEnabled(isShowAllWarningsEnabled: Boolean) {
    value = value.copy(isShowAllWarningsEnabled = isShowAllWarningsEnabled)
}

fun MutableStateFlow<RallyScreenUIState>.setHallOfFame(hallOfFame: List<HallOfFame>) {
    value = value.copy(hallOfFame = hallOfFame)
}

fun MutableStateFlow<RallyScreenUIState>.setIsHallOfFameLoading(isHallOfFameLoading: Boolean) {
    value = value.copy(isHallOfFameLoading = isHallOfFameLoading)
}

fun MutableStateFlow<RallyScreenUIState>.setTheme(theme: Theme) {
    value = value.copy(theme = theme)
}

fun MutableStateFlow<RallyScreenUIState>.setFontSizeFactor(fontSizeFactor: FontSizeFactor) {
    value = value.copy(fontSizeFactor = fontSizeFactor)
}

fun MutableStateFlow<RallyScreenUIState>.setIsShowAllActivitiesEnabled(isShowAllActivitiesEnabled: Boolean) {
    value = value.copy(isShowAllActivitiesEnabled = isShowAllActivitiesEnabled)
}

fun MutableStateFlow<RallyScreenUIState>.setIsSettingsBottomSheetVisible(isSettingsBottomSheetVisible: Boolean) {
    value = value.copy(isSettingsBottomSheetVisible = isSettingsBottomSheetVisible)
}

fun MutableStateFlow<RallyScreenUIState>.setIsShowAllBreakingNewsEnabled(isShowBreakingNewsEnabled: Boolean) {
    value = value.copy(isShowAllBreakingNewsEnabled = isShowBreakingNewsEnabled)
}

fun MutableStateFlow<RallyScreenUIState>.setAreActivitiesCollapsed(areActivitiesCollapsed: Boolean) {
    value = value.copy(areActivitiesCollapsed = areActivitiesCollapsed)
}