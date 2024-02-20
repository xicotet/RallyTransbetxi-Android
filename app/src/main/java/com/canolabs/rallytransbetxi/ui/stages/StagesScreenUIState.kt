package com.canolabs.rallytransbetxi.ui.stages

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import kotlinx.coroutines.flow.MutableStateFlow

data class StagesScreenUIState(
    val stages: List<Stage> = emptyList(),
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<StagesScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<StagesScreenUIState>.setStages(stages: List<Stage>) {
    value = value.copy(stages = stages)
}