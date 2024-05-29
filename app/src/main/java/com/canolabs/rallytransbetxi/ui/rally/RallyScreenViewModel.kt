package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import com.canolabs.rallytransbetxi.domain.entities.FontSizeFactor
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.domain.entities.Theme
import com.canolabs.rallytransbetxi.domain.usecases.GetActivitiesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetFontSizeFactorSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetLanguageSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNewsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetProfileSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetThemeSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.InsertSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RallyScreenViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val insertSettingsUseCase: InsertSettingsUseCase,
    private val getLanguageSettingsUseCase: GetLanguageSettingsUseCase,
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase,
    private val getProfileSettingsUseCase: GetProfileSettingsUseCase,
    private val getFontSizeFactorSettingsUseCase: GetFontSizeFactorSettingsUseCase
) : ViewModel() {

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

    fun fetchProfileSettings() {
        viewModelScope.launch {
            val profile = getProfileSettingsUseCase.invoke()
            _state.setDirectionsProfile(DirectionsProfile.entries.find { it.getDatabaseName() == profile }!!)
        }
    }

    fun fetchThemeSettings() {
        viewModelScope.launch {
            val theme = getThemeSettingsUseCase.invoke()
            _state.setTheme(Theme.entries.find {
                it.getDatabaseName() == theme
            }!!)
        }
    }

    fun fetchFontSizeFactorSettings() {
        viewModelScope.launch {
            val fontSizeFactor = getFontSizeFactorSettingsUseCase.invoke()
            _state.setFontSizeFactor(FontSizeFactor.entries.find {
                it.value() == fontSizeFactor
            }!!)
        }
    }

    suspend fun updateInitialThemeState(darkThemeState: MutableState<Boolean>, isSystemInDarkTheme: Boolean) {
        fetchThemeSettings()
        while (state.value.theme == null) {
            delay(100)
        }
        darkThemeState.value = when (state.value.theme) {
            Theme.LIGHT ->  false
            Theme.DARK -> true
            Theme.AUTO -> isSystemInDarkTheme
            else -> isSystemInDarkTheme
        }
    }

    fun fetchLanguageSettings() {
        viewModelScope.launch {
            val language = getLanguageSettingsUseCase.invoke()
            _state.setLanguage(Language.entries.find {
               it.getDatabaseName() == language
            }!!)
        }
    }

    fun insertSettings() {
        viewModelScope.launch {
            val language = _state.value.language?.getDatabaseName()!!
            val theme = _state.value.theme?.getDatabaseName()!!
            val profile = _state.value.directionsProfile?.getDatabaseName()!!
            val fontSizeFactor = _state.value.fontSizeFactor?.value()!!

            insertSettingsUseCase.invoke(language, theme, profile, fontSizeFactor)
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

    fun toggleShowAllBreakingNews() {
        _state.setIsShowAllBreakingNewsEnabled(!_state.value.isShowAllBreakingNewsEnabled)
    }

    fun setIsSettingsBottomSheetVisible(isVisible: Boolean) {
        _state.setIsSettingsBottomSheetVisible(isVisible)
    }

    fun setLanguage(language: Language) {
        _state.setLanguage(language)
    }

    fun setTheme(theme: Theme) {
        _state.setTheme(theme)
    }

    fun setProfile(profile: DirectionsProfile) {
        _state.setDirectionsProfile(profile)
    }

    fun setFontSizeFactor(fontSizeFactor: FontSizeFactor) {
        _state.setFontSizeFactor(fontSizeFactor)
    }
}