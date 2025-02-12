package com.canolabs.rallytransbetxi.ui.rally

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.data.models.responses.PlaceResponse
import com.canolabs.rallytransbetxi.data.models.responses.Statement
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import com.canolabs.rallytransbetxi.domain.entities.FontSizeFactor
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.domain.entities.Theme
import com.canolabs.rallytransbetxi.domain.usecases.CanAccessToAppUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetActivitiesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreActivitiesCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreNewsCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreStatementsCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetBetxiRestaurantsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetFontSizeFactorSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetHallOfFameUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNewsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNotificationPermissionCounterUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNumberOfSponsorsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetProfileSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetThemeSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStatementsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.InsertSettingsUseCase
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.Constants.Companion.PLACES_NEARBY_SEARCH_MANUAL_EXCLUDED_RESTAURANTS
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.util.Locale

class RallyScreenViewModel : ViewModel() {

    private var _state = MutableStateFlow(RallyScreenUIState())
    val state: StateFlow<RallyScreenUIState> = _state.asStateFlow()

    private var _blockApp = MutableStateFlow(false)
    val blockApp: StateFlow<Boolean> = _blockApp.asStateFlow()

    private val getNewsUseCase: GetNewsUseCase by inject(GetNewsUseCase::class.java)
    private val getStatementsUseCase: GetStatementsUseCase by inject(GetStatementsUseCase::class.java)
    private val getHallOfFameUseCase: GetHallOfFameUseCase by inject(GetHallOfFameUseCase::class.java)
    private val getActivitiesUseCase: GetActivitiesUseCase by inject(GetActivitiesUseCase::class.java)
    private val insertSettingsUseCase: InsertSettingsUseCase by inject(InsertSettingsUseCase::class.java)
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase by inject(GetThemeSettingsUseCase::class.java)
    private val getProfileSettingsUseCase: GetProfileSettingsUseCase by inject(GetProfileSettingsUseCase::class.java)
    private val getFontSizeFactorSettingsUseCase: GetFontSizeFactorSettingsUseCase by inject(GetFontSizeFactorSettingsUseCase::class.java)
    private val getNotificationPermissionCounterUseCase: GetNotificationPermissionCounterUseCase by inject(GetNotificationPermissionCounterUseCase::class.java)
    private val getAreActivitiesCollapsed: GetAreActivitiesCollapsedUseCase by inject(GetAreActivitiesCollapsedUseCase::class.java)
    private val getAreNewsCollapsedUseCase: GetAreNewsCollapsedUseCase  by inject(GetAreNewsCollapsedUseCase::class.java)
    private val getAreStatementsCollapsedUseCase: GetAreStatementsCollapsedUseCase by inject(GetAreStatementsCollapsedUseCase::class.java)
    private val canAccessToAppUseCase: CanAccessToAppUseCase by inject(CanAccessToAppUseCase::class.java)
    private val getBetxiRestaurantsUseCase: GetBetxiRestaurantsUseCase by inject(GetBetxiRestaurantsUseCase::class.java)
    private val getNumberOfSponsorsUseCase: GetNumberOfSponsorsUseCase by inject(GetNumberOfSponsorsUseCase::class.java)

    fun checkAppVersion() {
        viewModelScope.launch {
            _blockApp.value = !canAccessToAppUseCase.invoke()
        }
    }

    fun fetchNews() {
        viewModelScope.launch {
            _state.setIsLoading(true)

            val news = getNewsUseCase.invoke()
            val newsOrderedByDate = news.sortedByDescending { it.date?.seconds ?: 0L }
            _state.setNews(newsOrderedByDate)

            _state.setIsLoading(false)
        }
    }

    fun fetchActivities() {
        viewModelScope.launch {
            _state.setIsLoading(true)

            val activities = getActivitiesUseCase.invoke()
            val activitiesOrderedByIndex = activities.sortedBy {
                it.index
            }
            _state.setActivities(activitiesOrderedByIndex)

            _state.setIsLoading(false)
        }
    }

    fun fetchStatements() {
        viewModelScope.launch {
            _state.setIsLoading(true)

            val statements = getStatementsUseCase.invoke()
            _state.setStatements(statements)

            if (statements.any { it.needsToBePromptedAsDialog }) {
                _state.setIsDialogShowing(true)
                _state.setStatementShownOnDialog(statements.first { it.needsToBePromptedAsDialog })
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

    fun fetchNumberOfSponsors() {
        viewModelScope.launch {
            val numberOfSponsors = getNumberOfSponsorsUseCase.invoke()
            _state.setNumberOfSponsors(numberOfSponsors)
        }
    }

    fun fetchRestaurants() {
        viewModelScope.launch {
            // Start fetching restaurants by popularity and distance concurrently
            val distanceDeferred = async { fetchRestaurantsByDistance() }
            val popularityDeferred = async { fetchRestaurantsByPopularity() }

            // Wait for both results to complete
            val allRestaurants = listOf(
                distanceDeferred.await(),
                popularityDeferred.await()
            ).flatten()

            // Filter out restaurants with no displayName or no coordinates at all
            val notNullRestaurants = allRestaurants.filter {
                it.displayName != null &&
                        it.location?.latitude != null && it.location.longitude != null
            }
            // Filter out duplicates based on some unique identifier, e.g., place ID
            val uniqueRestaurants = notNullRestaurants.distinctBy { it.displayName?.text  }

            // Filter restaurants without an image and no ratings
            val filteredRestaurants = uniqueRestaurants.filter {
                it.photos?.isNotEmpty() == true && it.rating != null
            }

            // Filter out manually excluded restaurants
            val manuallyExcludedRestaurants = PLACES_NEARBY_SEARCH_MANUAL_EXCLUDED_RESTAURANTS.split(",")
            val finalRestaurants = filteredRestaurants.filter {
                it.displayName?.text !in manuallyExcludedRestaurants
            }

            Log.w("RallyScreenViewModel", "Fetched ${finalRestaurants.size} unique restaurants after filtering")

            // Update the state with the filtered list
            _state.setRestaurants(finalRestaurants)
        }
    }

    private suspend fun fetchRestaurantsByPopularity(): List<PlaceResponse>{
        return getBetxiRestaurantsUseCase.invoke(
            rankPreference = Constants.PLACES_NEARBY_SEARCH_RANK_BY_POPULARITY,
            languageCode = state.value.language?.getLanguageCode(),
        ).also {
            Log.w("RallyScreenViewModel", "Fetched ${it.size} restaurants by popularity")
        }
    }

    private suspend fun fetchRestaurantsByDistance(): List<PlaceResponse> {
        return getBetxiRestaurantsUseCase.invoke(
            rankPreference = Constants.PLACES_NEARBY_SEARCH_RANK_BY_DISTANCE,
            languageCode = state.value.language?.getLanguageCode(),
        ).also {
            Log.w("RallyScreenViewModel", "Fetched ${it.size} restaurants by distance")
        }
    }

    /*
     * App Settings related functions
     */

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

    fun fetchNotificationPermissionCounter() {
        viewModelScope.launch {
            val counter = getNotificationPermissionCounterUseCase.invoke()
            val updatedCounter = counter + 1
            _state.setNotificationPermissionCounter(updatedCounter)
        }
    }

    fun fetchAreStatementsCollapsed() {
        viewModelScope.launch {
            _state.setAreStatementsCollapsed(getAreStatementsCollapsedUseCase.invoke())
        }
    }

    fun fetchAreNewsCollapsed() {
        viewModelScope.launch {
            _state.setAreBreakingNewsCollapsed(getAreNewsCollapsedUseCase.invoke())
        }
    }

    fun fetchAreActivitiesCollapsed() {
        viewModelScope.launch {
            _state.setAreActivitiesCollapsed(getAreActivitiesCollapsed.invoke())
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

    suspend fun updateInitialProfile() {
        fetchProfileSettings()
        while (state.value.directionsProfile == null) {
            delay(100)
        }
    }

    fun insertSettings() {
        viewModelScope.launch {
            val theme = _state.value.theme?.getDatabaseName()!!
            val profile = _state.value.directionsProfile?.getDatabaseName()!!
            val fontSizeFactor = _state.value.fontSizeFactor?.value()!!
            val notificationPermissionCounter = _state.value.notificationPermissionCounter!!
            val areStatements = _state.value.areStatementsCollapsed
            val areNewsCollapsed = _state.value.areBreakingNewsCollapsed
            val areActivitiesCollapsed = _state.value.areActivitiesCollapsed

            insertSettingsUseCase.invoke(
                theme,
                profile,
                fontSizeFactor,
                notificationPermissionCounter,
                areStatements,
                areNewsCollapsed,
                areActivitiesCollapsed
            )
        }
    }

    /*
     * Setters and toggles that can be called from the UI
     */

    fun toggleStatements() {
        _state.setAreStatementsCollapsed(!_state.value.areStatementsCollapsed)
    }

    fun toggleBreakingNews() {
        _state.setAreBreakingNewsCollapsed(!_state.value.areBreakingNewsCollapsed)
    }

    fun toggleActivities() {
        _state.setAreActivitiesCollapsed(!_state.value.areActivitiesCollapsed)
    }

    fun toggleShowAllStatements() {
        _state.setIsShowAllStatementsEnabled(!_state.value.isShowAllStatementsEnabled)
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

    fun setStatementShownOnDialog(statement: Statement) {
        _state.setStatementShownOnDialog(statement)
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

    fun setNotificationPermissionCounter(counter: Int) {
        _state.setNotificationPermissionCounter(counter)
    }

    fun shouldShowNotificationPermissionSheet(): Boolean {
        val counter = state.value.notificationPermissionCounter!!
        val nextInterval = getArbitraryInterval(counter)

        // Show bottom sheet only when counter matches the interval
        return counter == nextInterval
    }

    // Arbitrary with a cap at 256
    private fun getArbitraryInterval(counter: Int): Int {
        val intervals = listOf(1, 5, 10, 18, 32, 64, 128, 256)
        return intervals.find { it >= counter } ?: 256
    }
}