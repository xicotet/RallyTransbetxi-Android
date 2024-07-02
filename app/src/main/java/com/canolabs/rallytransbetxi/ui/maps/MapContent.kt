package com.canolabs.rallytransbetxi.ui.maps

import android.Manifest
import android.location.Location
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.miscellaneous.bitmapDescriptorFromVector
import com.canolabs.rallytransbetxi.ui.results.BottomSheetStageResults
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
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
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    stageAcronym: String,
    darkThemeState: MutableState<Boolean>,
    scaffoldPadding: PaddingValues,
    mapsViewModel: MapsScreenViewModel,
    resultsViewModel: ResultsScreenViewModel,
    navController: NavController
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val mapStyleOptions = if (darkThemeState.value) {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.night_map_style)
    } else {
        null
    }

    mapsViewModel.setMapProperties(
        state.mapProperties.copy(mapStyleOptions = mapStyleOptions)
    )

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
                properties = state.mapProperties,
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
                    AnimateCameraToUserLocation(
                        location = state.location,
                        cameraPositionState = cameraPositionState
                    )
                else
                    AnimateCameraToDirections(
                        directions = state.directions,
                        cameraPositionState = cameraPositionState
                    )


                if (state.stage.geoPoints == null) {
                    AnimateCameraToBetxi(betxi = betxi, cameraPositionState = cameraPositionState)
                } else if (state.location == null && state.directions.isEmpty()) {
                    // Animate the camera to the first geo point of the stage
                    AnimateCameraToFirstStagePoint(
                        stage = state.stage,
                        cameraPositionState = cameraPositionState
                    )
                }
            }

            AssistChip(
                onClick = { mapsViewModel.setIsResultsBottomSheetVisible(true) },
                label = {
                    Text(
                        text = stringResource(id = R.string.results),
                        fontFamily = robotoFamily
                    )
                },
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

            var expanded by remember { mutableStateOf(false) }

            Surface(
                shadowElevation = 4.dp,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(scaffoldPadding)
                    .padding(16.dp)
                    .background(Color.White, CircleShape)
            ) {
                IconButton(
                    onClick = {
                        expanded = true
                    },
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.layers),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.standard_map)) },
                        onClick = {
                            mapsViewModel.setMapProperties(
                                state.mapProperties.copy(mapType = MapType.NORMAL)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.standard_map),
                                contentDescription = null
                            )
                        },
                        trailingIcon = if (state.mapProperties.mapType == MapType.NORMAL) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )
                            }
                        } else {
                            null
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.satellite_map)) },
                        onClick = {
                            mapsViewModel.setMapProperties(
                                state.mapProperties.copy(mapType = MapType.SATELLITE)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.satellite),
                                contentDescription = null
                            )
                        },
                        trailingIcon = if (state.mapProperties.mapType == MapType.SATELLITE) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )
                            }
                        } else {
                            null
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.terrain_map)) },
                        onClick = {
                            mapsViewModel.setMapProperties(
                                state.mapProperties.copy(mapType = MapType.TERRAIN)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.park),
                                contentDescription = null
                            )
                        },
                        trailingIcon = if (state.mapProperties.mapType == MapType.TERRAIN) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }

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
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                        // Move the camera to the user's location
                        state.location?.let {
                            val targetPosition = LatLng(it.latitude, it.longitude)
                            val cameraUpdate =
                                CameraUpdateFactory.newLatLngZoom(targetPosition, 15f)
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

            if (state.hasPressedDirectionsButton) {
                Surface(
                    shadowElevation = 4.dp,
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp, 16.dp, 16.dp, 40.dp)
                        .background(Color.White, CircleShape)
                ) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            if (state.directions.isNotEmpty()) {
                                val gmmIntentUri =
                                    Uri.parse("google.navigation:q=${state.directions.last()[1]},${state.directions.last()[0]}")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                context.startActivity(mapIntent)
                            } else {
                                // Directions are not available yet
                            }
                        },
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.navigation),
                                "Extended floating action button."
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(id = R.string.navigate),
                                fontFamily = robotoFamily
                            )
                        },
                    )
                }
            } else {
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
                            coroutineScope.launch {
                                Log.d("MapContent", "Directions button pressed")
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                                if (state.locationPermissionIsGranted && state.stage.geoPoints != null) {
                                    Log.d("MapContent", "Directions button pressed and location permission granted")
                                    delay(1000)
                                    mapsViewModel.setHasPressedDirectionsButton(true)
                                    mapsViewModel.getDirections()
                                } else {
                                    Log.d("MapContent", "Directions button pressed but location permission denied")
                                }
                            }

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
        }

        val resultsState by resultsViewModel.state.collectAsState()

        if (state.isResultsBottomSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = {
                    coroutineScope.launch {
                        mapsViewModel.setIsResultsBottomSheetVisible(false)
                        bottomSheetState.hide()
                    }
                },
            ) {
                BottomSheetStageResults(
                    resultsState = resultsState,
                    mapsState = state,
                    viewModel = resultsViewModel,
                    isComingFromMaps = true,
                    navController = navController
                )
            }
        } else if (state.isPermissionDeniedBottomSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                dragHandle = {},
                onDismissRequest = {
                    coroutineScope.launch {
                        mapsViewModel.setIsPermissionDeniedBottomSheetVisible(false)
                        bottomSheetState.hide()
                    }
                },
            ) {
                BottomSheetPermissionDenied(
                    onOmitButtonPressed = {
                        coroutineScope.launch {
                            mapsViewModel.setIsPermissionDeniedBottomSheetVisible(false)
                            bottomSheetState.hide()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AnimateCameraToBetxi(
    betxi: LatLng,
    cameraPositionState: CameraPositionState
) {
    val targetPosition = LatLng(betxi.latitude, betxi.longitude)
    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPosition, 15f)
    LaunchedEffect(cameraUpdate) {
        delay(500)
        cameraPositionState.animate(cameraUpdate, durationMs = 2000)
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