package com.canolabs.rallytransbetxi.ui.maps

import android.location.Location
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.flow.MutableStateFlow

data class MapsScreenUIState(
    val stage: Stage = Stage(),
    val mapProperties: MapProperties = MapProperties(),
    val location: Location? = null,
    val directions: List<List<Double>> = emptyList(),
    val hasPressedDirectionsButton: Boolean = false,
    val uiSettings: MapUiSettings = MapUiSettings(),
    val isBottomSheetVisible: Boolean = false,
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<MapsScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<MapsScreenUIState>.setStage(stage: Stage) {
    value = value.copy(stage = stage)
}

fun MutableStateFlow<MapsScreenUIState>.setDirections(directions: List<List<Double>>) {
    value = value.copy(directions = directions)
}

fun MutableStateFlow<MapsScreenUIState>.setIsBottomSheetVisible(isBottomSheetVisible: Boolean) {
    value = value.copy(isBottomSheetVisible = isBottomSheetVisible)
}

fun MutableStateFlow<MapsScreenUIState>.setLocation(location: Location?) {
    value = value.copy(location = location)
}

fun MutableStateFlow<MapsScreenUIState>.setHasPressedDirectionsButton(hasPressedDirectionsButton: Boolean) {
    value = value.copy(hasPressedDirectionsButton = hasPressedDirectionsButton)
}

fun MutableStateFlow<MapsScreenUIState>.setMapProperties(mapProperties: MapProperties) {
    value = value.copy(mapProperties = mapProperties)
}

fun MutableStateFlow<MapsScreenUIState>.setUiSettings(uiSettings: MapUiSettings) {
    value = value.copy(uiSettings = uiSettings)
}