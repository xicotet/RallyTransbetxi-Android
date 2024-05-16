package com.canolabs.rallytransbetxi.ui.maps

import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.miscellaneous.bitmapDescriptorFromVector
import com.canolabs.rallytransbetxi.ui.results.BottomSheetStageResults
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapContent(
    state: MapsScreenUIState,
    cameraPositionState: CameraPositionState,
    betxi: LatLng,
    stageAcronym: String,
    scaffoldPadding: PaddingValues,
    mapsViewModel: MapsScreenViewModel,
    resultsViewModel: ResultsScreenViewModel
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            mapsViewModel.getLocation()
        }
    }

    LaunchedEffect(Unit) {
        resultsViewModel.fetchStagesResults(stageAcronym)
    }

    Column {
        if (state.isLoading) {
            LinearProgressIndicator(
                // dynamic progress value
                modifier = Modifier
                    .fillMaxWidth() // fill the width of the parent
                    .padding(scaffoldPadding), // add some padding
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                uiSettings = state.uiSettings,
                cameraPositionState = cameraPositionState
            ) {
                Polyline(
                    points = state.stage.geoPoints?.map {
                        LatLng(it.latitude, it.longitude)
                    } ?: emptyList(),
                    color = MaterialTheme.colorScheme.primary
                )

                Marker(
                    state = MarkerState(state.stage.geoPoints?.first()?.let {
                        LatLng(it.latitude, it.longitude)
                    } ?: betxi),
                    title = state.stage.name,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )

                Marker(
                    state = MarkerState(state.stage.geoPoints?.last()?.let {
                        LatLng(it.latitude, it.longitude)
                    } ?: betxi),
                    title = state.stage.name,
                )

                PrintUserLocation(location = state.location)
                PrintDirections(directions = state.directions)

                if (state.directions.isEmpty())
                    AnimateCameraToUserLocation(location = state.location, cameraPositionState = cameraPositionState)
                else
                    AnimateCameraToDirections(directions = state.directions, cameraPositionState = cameraPositionState)


                if (state.location == null && state.directions.isEmpty()) {
                    // Animate the camera to the first geo point of the stage
                    AnimateCameraToFirstStagePoint(stage = state.stage, cameraPositionState = cameraPositionState)
                }
            }

            AssistChip(
                onClick = { mapsViewModel.setIsBottomSheetVisible(true) },
                label = { Text(text = stringResource(id = R.string.results)) },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.sports_score),
                        contentDescription = null
                    )
                },
                colors = AssistChipDefaults.assistChipColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                    labelColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Surface(
                shadowElevation = 4.dp,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp, 16.dp, 16.dp, 120.dp)
                    .background(Color.White, CircleShape)
            ) {
                IconButton(
                    onClick = {
                        permissionLauncher.launch(
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        // Move the camera to the user's location
                        state.location?.let {
                            val targetPosition = LatLng(it.latitude, it.longitude)
                            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPosition, 15f)
                            coroutineScope.launch {
                                cameraPositionState.animate(cameraUpdate, durationMs = 2000)
                            }
                        }
                    },
                    modifier = Modifier.size(60.dp),
                ) {
                    Icon(
                        painter = if (state.location == null) {
                            painterResource(id = R.drawable.my_location_unknown)
                        } else {
                            painterResource(id = R.drawable.my_location_known)
                        },
                        contentDescription = null,
                        tint = if (state.location == null) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Surface(
                shadowElevation = 4.dp,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp, 16.dp, 16.dp, 40.dp)
                    .background(Color.White, CircleShape)
            ) {
                IconButton(
                    onClick = {
                        permissionLauncher.launch(
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        mapsViewModel.getDirections()
                    },
                    modifier = Modifier.size(60.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.directions_outlined),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        val resultsState by resultsViewModel.state.collectAsState()

        if (state.isBottomSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = {
                    coroutineScope.launch {
                        mapsViewModel.setIsBottomSheetVisible(false)
                        bottomSheetState.hide()
                    }
                },
            ) {
                BottomSheetStageResults(
                    state = resultsState,
                    viewModel = resultsViewModel,
                    isComingFromMaps = true,
                )
            }
        }
    }
}

@Composable
fun AnimateCameraToFirstStagePoint(
    stage: Stage,
    cameraPositionState: CameraPositionState
) {
    stage.geoPoints?.first()?.let { geoPoint ->
        val targetPosition = LatLng(geoPoint.latitude, geoPoint.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPosition, 15f)
        LaunchedEffect(cameraUpdate) {
            delay(500)
            cameraPositionState.animate(cameraUpdate, durationMs = 2000)
        }
    }
}

@Composable
fun PrintDirections(
    directions: List<List<Double>>,
) {
    Polyline(
        points = directions.map { LatLng(it[1], it[0]) },
        color = Color.Blue
    )
}

@Composable
fun AnimateCameraToDirections(
    directions: List<List<Double>>,
    cameraPositionState: CameraPositionState
) {
    if (directions.isNotEmpty()) {
        // Obtain a center point between the first and last points of state.directions
        LaunchedEffect(Unit) {
            val (zoomLevel, center) = calculateZoomLevel(directions)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(center, zoomLevel)
            cameraPositionState.animate(cameraUpdate, durationMs = 2000)
        }
    }
}

@Composable
fun PrintUserLocation(
    location: Location?,
) {
    val context = LocalContext.current
    val icon = context.bitmapDescriptorFromVector(R.drawable.location_circle, 0.7f)

    Marker(
        state = MarkerState(
            LatLng(
            location?.latitude ?: 0.0,
                location?.longitude ?: 0.0
            )
        ),
        title = "User Location",
        icon = icon,

    )
}

@Composable
fun AnimateCameraToUserLocation(
    location: Location?,
    cameraPositionState: CameraPositionState
) {
    // Animate the camera to the user's location
    val targetPosition = LatLng(
        location?.latitude ?: 0.0,
        location?.longitude ?: 0.0
    )
    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPosition, 15f)
    LaunchedEffect(cameraUpdate) {
        delay(500)
        cameraPositionState.animate(cameraUpdate, durationMs = 2000)
    }
}

fun calculateZoomLevel(directions: List<List<Double>>): Pair<Float, LatLng> {
    val bounds = LatLngBounds.builder()
        .include(LatLng(directions.first()[1], directions.first()[0]))
        .include(LatLng(directions.last()[1], directions.last()[0]))
        .build()
    val center = LatLng(
        (bounds.northeast.latitude + bounds.southwest.latitude) / 2,
        (bounds.northeast.longitude + bounds.southwest.longitude) / 2
    )

    val distance = FloatArray(1)
    Location.distanceBetween(
        bounds.southwest.latitude,
        bounds.southwest.longitude,
        bounds.northeast.latitude,
        bounds.northeast.longitude,
        distance
    )

    // Adjust the zoom level based on the distance
    val zoomLevel = when {
        distance[0] > 10000f -> 10f
        distance[0] > 5000f -> 12f
        distance[0] > 2000f -> 14f
        else -> 15f
    }

    return Pair(zoomLevel, center)
}