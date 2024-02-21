package com.canolabs.rallytransbetxi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import javax.inject.Inject

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

class MainActivityViewModelFactory @Inject constructor(
    private val stagesViewModelFactory: StagesViewModelFactory
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StagesScreenViewModel::class.java)) {
            return stagesViewModelFactory.create(modelClass)
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}