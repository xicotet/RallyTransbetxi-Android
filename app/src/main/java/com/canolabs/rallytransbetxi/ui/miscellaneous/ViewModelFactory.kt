package com.canolabs.rallytransbetxi.ui.miscellaneous

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetTeamsUseCase
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreenViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class StagesViewModelFactory @Inject constructor(
    private val getStagesUseCase: GetStagesUseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StagesScreenViewModel::class.java)) {
            return StagesScreenViewModel(getStagesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Suppress("UNCHECKED_CAST")
class TeamsViewModelFactory @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamsScreenViewModel::class.java)) {
            return TeamsScreenViewModel(getTeamsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Suppress("UNCHECKED_CAST")
class ResultsViewModelFactory @Inject constructor(
    private val getGlobalResultsUseCase: GetGlobalResultsUseCase,
    private val getStagesUseCase: GetStagesUseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsScreenViewModel::class.java)) {
            return ResultsScreenViewModel(getGlobalResultsUseCase, getStagesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivityViewModelFactory @Inject constructor(
    private val stagesViewModelFactory: StagesViewModelFactory,
    private val teamsViewModelFactory: TeamsViewModelFactory,
    private val resultsViewModelFactory: ResultsViewModelFactory
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
            // Add other ViewModel class checks here
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}