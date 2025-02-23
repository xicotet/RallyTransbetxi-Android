package com.canolabs.rallytransbetxi.ui.miscellaneous

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.canolabs.rallytransbetxi.domain.usecases.CanAccessToAppUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetActivitiesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreActivitiesCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreNewsCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreStatementsCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetBetxiRestaurantsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetDirectionsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetFontSizeFactorSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalRaceWarningUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetHallOfFameUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNewsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNotificationPermissionCounterUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNumberOfSponsorsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetProfileSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetRestaurantsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStageByAcronymUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStageRaceWarningUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetTeamsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetThemeSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStatementsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.InsertSettingsUseCase
import com.canolabs.rallytransbetxi.ui.maps.MapsScreenViewModel
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreenViewModel
import javax.inject.Inject

class RallyViewModelFactory @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val getStatementsUseCase: GetStatementsUseCase,
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getHallOfFameUseCase: GetHallOfFameUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val insertSettingsUseCase: InsertSettingsUseCase,
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase,
    private val getProfileSettingsUseCase: GetProfileSettingsUseCase,
    private val getFontSizeFactorSettingsUseCase: GetFontSizeFactorSettingsUseCase,
    private val getNotificationPermissionCounterUseCase: GetNotificationPermissionCounterUseCase,
    private val getAreActivitiesCollapsed: GetAreActivitiesCollapsedUseCase,
    private val getAreNewsCollapsedUseCase: GetAreNewsCollapsedUseCase,
    private val getAreStatementsCollapsedUseCase: GetAreStatementsCollapsedUseCase,
    private val canAccessToAppUseCase: CanAccessToAppUseCase,
    private val getBetxiRestaurantsUseCase: GetBetxiRestaurantsUseCase,
    private val getNumberOfSponsorsUseCase: GetNumberOfSponsorsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RallyScreenViewModel::class.java) -> {
                RallyScreenViewModel(
                    getNewsUseCase,
                    getStatementsUseCase,
                    getRestaurantsUseCase,
                    getHallOfFameUseCase,
                    getActivitiesUseCase,
                    insertSettingsUseCase,
                    getThemeSettingsUseCase,
                    getProfileSettingsUseCase,
                    getFontSizeFactorSettingsUseCase,
                    getNotificationPermissionCounterUseCase,
                    getAreActivitiesCollapsed,
                    getAreNewsCollapsedUseCase,
                    getAreStatementsCollapsedUseCase,
                    canAccessToAppUseCase,
                    getBetxiRestaurantsUseCase,
                    getNumberOfSponsorsUseCase
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

@Suppress("UNCHECKED_CAST")
class StagesViewModelFactory @Inject constructor(
    private val getStagesUseCase: GetStagesUseCase,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StagesScreenViewModel::class.java)) {
            return StagesScreenViewModel(
                getStagesUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Suppress("UNCHECKED_CAST")
class MapsViewModelFactory @Inject constructor(
    private val getStageByAcronymUseCase: GetStageByAcronymUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getProfileSettingsUseCase: GetProfileSettingsUseCase,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsScreenViewModel::class.java)) {
            return MapsScreenViewModel(
                getStageByAcronymUseCase,
                getDirectionsUseCase,
                getProfileSettingsUseCase,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Suppress("UNCHECKED_CAST")
class TeamsViewModelFactory @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase,
    private val getGlobalResultsUseCase: GetGlobalResultsUseCase,
    private val getStagesResultsUseCase: GetStagesResultsUseCase,
    private val getStagesUseCase: GetStagesUseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamsScreenViewModel::class.java)) {
            return TeamsScreenViewModel(
                getTeamsUseCase,
                getGlobalResultsUseCase,
                getStagesUseCase,
                getStagesResultsUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Suppress("UNCHECKED_CAST")
class ResultsViewModelFactory @Inject constructor(
    private val getGlobalResultsUseCase: GetGlobalResultsUseCase,
    private val getStagesResultsUseCase: GetStagesResultsUseCase,
    private val getGlobalRaceWarningUseCase: GetGlobalRaceWarningUseCase,
    private val getStageRaceWarningUseCase: GetStageRaceWarningUseCase,
    private val getStagesUseCase: GetStagesUseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsScreenViewModel::class.java)) {
            return ResultsScreenViewModel(
                getGlobalResultsUseCase,
                getStagesResultsUseCase,
                getGlobalRaceWarningUseCase,
                getStageRaceWarningUseCase,
                getStagesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivityViewModelFactory @Inject constructor(
    private val stagesViewModelFactory: StagesViewModelFactory,
    private val teamsViewModelFactory: TeamsViewModelFactory,
    private val resultsViewModelFactory: ResultsViewModelFactory,
    private val mapsViewModelFactory: MapsViewModelFactory,
    private val rallyViewModelFactory: RallyViewModelFactory
    // Add other ViewModelFactories here
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StagesScreenViewModel::class.java) -> {
                stagesViewModelFactory.create(modelClass)
            }

            modelClass.isAssignableFrom(TeamsScreenViewModel::class.java) -> {
                teamsViewModelFactory.create(modelClass)
            }

            modelClass.isAssignableFrom(ResultsScreenViewModel::class.java) -> {
                resultsViewModelFactory.create(modelClass)
            }

            modelClass.isAssignableFrom(MapsScreenViewModel::class.java) -> {
                mapsViewModelFactory.create(modelClass)
            }

            modelClass.isAssignableFrom(RallyScreenViewModel::class.java) -> {
                rallyViewModelFactory.create(modelClass)
            }
            // Add other ViewModel class checks here
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}