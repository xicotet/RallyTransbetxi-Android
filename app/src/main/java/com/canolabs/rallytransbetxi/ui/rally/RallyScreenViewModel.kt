package com.canolabs.rallytransbetxi.ui.rally

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.data.models.responses.Warning
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import com.canolabs.rallytransbetxi.domain.entities.FontSizeFactor
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.domain.entities.Theme
import com.canolabs.rallytransbetxi.domain.usecases.GetActivitiesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetFontSizeFactorSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetHallOfFameUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNewsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetProfileSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetRestaurantsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetThemeSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetWarningsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.InsertSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RallyScreenViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val getWarningsUseCase: GetWarningsUseCase,
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getHallOfFameUseCase: GetHallOfFameUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val insertSettingsUseCase: InsertSettingsUseCase,
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

    fun fetchWarnings() {
        viewModelScope.launch {
            _state.setIsLoading(true)

            val warnings = getWarningsUseCase.invoke()
            _state.setWarnings(warnings)

            if (warnings.any { it.needsToBePromptedAsDialog }) {
                _state.setIsDialogShowing(true)
                _state.setWarningShownOnDialog(warnings.first { it.needsToBePromptedAsDialog })
            }

            _state.setIsLoading(false)
        }
    }

    fun fetchHallOfFame() {
        viewModelScope.launch {
            _state.setIsHallOfFameLoading(true)

            val hallOfFame = getHallOfFameUseCase.invoke()
            val hallOfFameOrderedByDate = hallOfFame.sortedByDescending { it.year }
            _state.setHallOfFame(hallOfFameOrderedByDate)

            _state.setIsHallOfFameLoading(false)
        }
    }

    fun fetchRestaurants() {
        viewModelScope.launch {
            val restaurants = getRestaurantsUseCase.invoke()
            _state.setRestaurants(restaurants)
        }
    }

    fun fetchProfileSettings() {
        viewModelScope.launch {
            _state.setDirectionsProfile(getProfileSettingsUseCase.invoke())
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

    fun fetchLanguage(sharedPreferences: SharedPreferences) {
        viewModelScope.launch {
            val language = sharedPreferences.getString("SelectedLanguage", Locale.getDefault().language)
            if (language != null) {
                _state.setLanguage(Language.entries.find { it.getLanguageCode() == language }!!)
            }
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

    suspend fun updateInitialFontSizeFactor(fontScaleState: MutableState<Float>) {
        fetchFontSizeFactorSettings()
        while (state.value.fontSizeFactor == null) {
            delay(100)
        }
        fontScaleState.value = state.value.fontSizeFactor?.value() ?: 1f
    }

    fun insertSettings() {
        viewModelScope.launch {
            val theme = _state.value.theme?.getDatabaseName()!!
            val profile = _state.value.directionsProfile?.getDatabaseName()!!
            val fontSizeFactor = _state.value.fontSizeFactor?.value()!!

            insertSettingsUseCase.invoke(theme, profile, fontSizeFactor)
        }
    }

    fun toggleWarnings() {
        _state.setAreWarningsCollapsed(!_state.value.areWarningsCollapsed)
    }

    fun toggleBreakingNews() {
        _state.setAreBreakingNewsCollapsed(!_state.value.areBreakingNewsCollapsed)
    }

    fun toggleActivities() {
        _state.setAreActivitiesCollapsed(!_state.value.areActivitiesCollapsed)
    }

    fun toggleShowAllWarnings() {
        _state.setIsShowAllWarningsEnabled(!_state.value.isShowAllWarningsEnabled)
    }

    fun toggleShowAllActivities() {
        _state.setIsShowAllActivitiesEnabled(!_state.value.isShowAllActivitiesEnabled)
    }

    fun toggleShowAllBreakingNews() {
        _state.setIsShowAllBreakingNewsEnabled(!_state.value.isShowAllBreakingNewsEnabled)
    }

    fun setIsDialogShowing(isVisible: Boolean) {
        _state.setIsDialogShowing(isVisible)
    }

    fun setWarningShownOnDialog(warning: Warning) {
        _state.setWarningShownOnDialog(warning)
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