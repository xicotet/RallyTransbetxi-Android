package com.canolabs.rallytransbetxi.ui.maps

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canolabs.rallytransbetxi.domain.usecases.GetStageByAcronymUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsScreenViewModel @Inject constructor(
    private val getStageByAcronymUseCase: GetStageByAcronymUseCase,
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

    fun fetchStage(acronym: String) {
        viewModelScope.launch {
            _state.setIsLoading(true)
            _state.setStage(getStageByAcronymUseCase(acronym))
            _state.setIsLoading(false)
        }
    }

    fun setMapProperties(mapProperties: MapProperties) {
        _state.setMapProperties(mapProperties)
    }

    fun setUiSettings(uiSettings: MapUiSettings) {
        _state.setUiSettings(uiSettings)
    }

    fun setIsBottomSheetVisible(isBottomSheetVisible: Boolean) {
        _state.setIsBottomSheetVisible(isBottomSheetVisible)
    }
}