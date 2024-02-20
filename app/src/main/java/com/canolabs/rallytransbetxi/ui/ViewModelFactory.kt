package com.canolabs.rallytransbetxi.ui

import androidx.lifecycle.ViewModelProvider
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel

class StagesViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    //@Suppress("UNCHECKED_CAST")
    fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T =
        StagesScreenViewModel() as T
}