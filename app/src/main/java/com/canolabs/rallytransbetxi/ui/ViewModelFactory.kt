package com.canolabs.rallytransbetxi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import javax.inject.Inject

class StagesViewModelFactory @Inject constructor (
    private val getStagesUseCase: GetStagesUseCase
) : ViewModelProvider.NewInstanceFactory() {
    //@Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        StagesScreenViewModel(getStagesUseCase) as T
}