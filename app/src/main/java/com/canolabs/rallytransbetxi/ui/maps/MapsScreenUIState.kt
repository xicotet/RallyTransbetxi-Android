package com.canolabs.rallytransbetxi.ui.maps

import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.flow.MutableStateFlow

data class MapsScreenUIState(
    val stage: Stage = Stage(),
    val mapProperties: MapProperties = MapProperties(),
    val uiSettings: MapUiSettings = MapUiSettings(),
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<MapsScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<MapsScreenUIState>.setStage(stage: Stage) {
    value = value.copy(stage = stage)
}

fun MutableStateFlow<MapsScreenUIState>.setMapProperties(mapProperties: MapProperties) {
    value = value.copy(mapProperties = mapProperties)
}

fun MutableStateFlow<MapsScreenUIState>.setUiSettings(uiSettings: MapUiSettings) {
    value = value.copy(uiSettings = uiSettings)
}