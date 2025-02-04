package com.canolabs.rallytransbetxi.ui.maps

import android.location.Location
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.flow.MutableStateFlow

data class MapsScreenUIState(
    val stage: Stage = Stage(),
    val mapProperties: MapProperties = MapProperties(),
    val locationPermissionIsGranted: Boolean = false,
    val location: Location? = null,
    val directions: List<List<Double>> = emptyList(),
    val hasPressedDirectionsButton: Boolean = false,
    val uiSettings: MapUiSettings = MapUiSettings(),
    val isPermissionDeniedBottomSheetVisible: Boolean = false,
    val isResultsBottomSheetVisible: Boolean = false,
    val isGpsDialogVisible: Boolean = false,
    val language: Language? = null,
    val isLoadingDirections: Boolean = false,
    val directionsProfile: DirectionsProfile? = null,
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<MapsScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<MapsScreenUIState>.setIsLoadingDirections(isLoadingDirections: Boolean) {
    value = value.copy(isLoadingDirections = isLoadingDirections)
}

fun MutableStateFlow<MapsScreenUIState>.setStage(stage: Stage) {
    value = value.copy(stage = stage)
}

fun MutableStateFlow<MapsScreenUIState>.setDirections(directions: List<List<Double>>) {
    value = value.copy(directions = directions)
}

fun MutableStateFlow<MapsScreenUIState>.setIsResultsBottomSheetVisible(isBottomSheetVisible: Boolean) {
    value = value.copy(isResultsBottomSheetVisible = isBottomSheetVisible)
}

fun MutableStateFlow<MapsScreenUIState>.setIsPermissionDeniedBottomSheetVisible(isPermissionDeniedBottomSheetVisible: Boolean) {
    value = value.copy(isPermissionDeniedBottomSheetVisible = isPermissionDeniedBottomSheetVisible)
}

fun MutableStateFlow<MapsScreenUIState>.setDirectionsProfile(directionsProfile: DirectionsProfile) {
    value = value.copy(directionsProfile = directionsProfile)
}

fun MutableStateFlow<MapsScreenUIState>.setLanguage(language: Language) {
    value = value.copy(language = language)
}

fun MutableStateFlow<MapsScreenUIState>.setGpsDialogVisible(isGpsDialogVisible: Boolean) {
    value = value.copy(isGpsDialogVisible = isGpsDialogVisible)
}

fun MutableStateFlow<MapsScreenUIState>.setLocation(location: Location?) {
    value = value.copy(location = location)
}

fun MutableStateFlow<MapsScreenUIState>.setHasPressedDirectionsButton(hasPressedDirectionsButton: Boolean) {
    value = value.copy(hasPressedDirectionsButton = hasPressedDirectionsButton)
}

fun MutableStateFlow<MapsScreenUIState>.setLocationPermissionIsGranted(locationPermissionIsGranted: Boolean) {
    value = value.copy(locationPermissionIsGranted = locationPermissionIsGranted)
}

fun MutableStateFlow<MapsScreenUIState>.setMapProperties(mapProperties: MapProperties) {
    value = value.copy(mapProperties = mapProperties)
}

fun MutableStateFlow<MapsScreenUIState>.setUiSettings(uiSettings: MapUiSettings) {
    value = value.copy(uiSettings = uiSettings)
}