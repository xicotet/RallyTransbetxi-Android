package com.canolabs.rallytransbetxi.ui.maps

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.BuildConfig
import com.canolabs.rallytransbetxi.domain.usecases.GetDirectionsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStageByAcronymUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsScreenViewModel @Inject constructor(
    private val getStageByAcronymUseCase: GetStageByAcronymUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val application: Application
): ViewModel() {
    private var _state = MutableStateFlow(MapsScreenUIState())
    val state: StateFlow<MapsScreenUIState> = _state.asStateFlow()

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    fun getLocation() {
        viewModelScope.launch {
            if (ActivityCompat.checkSelfPermission(
                    application,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    application,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("MapsScreenViewModel", "No permissions")
                // TODO: Handle the case where the user has not granted the necessary permissions
                return@launch
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                loc?.let { _state.setLocation(it) }
            }
        }
    }

    fun getDirections() {
        viewModelScope.launch(Dispatchers.IO) {
            // We cannot get directions if we don't have the user location
            while (_state.value.location == null) {
                delay(100)
            }
            val directions = getDirectionsUseCase.execute(
                BuildConfig.DIRECTIONS_API_KEY,
                _state.value.location?.longitude.toString() + "," + _state.value.location?.latitude.toString(),
                _state.value.stage.geoPoints?.first()?.longitude.toString() + "," + _state.value.stage.geoPoints?.first()?.latitude.toString()
            )
            _state.setDirections(directions)
        }
    }

    fun fetchStage(acronym: String) {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setStage(getStageByAcronymUseCase(acronym))
            _state.setIsLoading(false)
        }
    }

    fun setLocationPermissionIsGranted(locationPermissionIsGranted: Boolean) {
        _state.setLocationPermissionIsGranted(locationPermissionIsGranted)
    }

    fun setUiSettings(uiSettings: MapUiSettings) {
        _state.setUiSettings(uiSettings)
    }

    fun setMapProperties(mapProperties: MapProperties) {
        _state.setMapProperties(mapProperties)
    }

    fun cleanDirections() {
        _state.setDirections(emptyList())
        setHasPressedDirectionsButton(false)
    }

    fun cleanLocation() {
        _state.setLocation(null)
    }

    fun setHasPressedDirectionsButton(hasPressedDirectionsButton: Boolean) {
        _state.setHasPressedDirectionsButton(hasPressedDirectionsButton)
    }

    fun setIsResultsBottomSheetVisible(isBottomSheetVisible: Boolean) {
        _state.setIsResultsBottomSheetVisible(isBottomSheetVisible)
    }

    fun setIsPermissionDeniedBottomSheetVisible(isPermissionDeniedBottomSheetVisible: Boolean) {
        _state.setIsPermissionDeniedBottomSheetVisible(isPermissionDeniedBottomSheetVisible)
    }
}