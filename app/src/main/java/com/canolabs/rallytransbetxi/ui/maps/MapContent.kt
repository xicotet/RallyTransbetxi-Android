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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import com.canolabs.rallytransbetxi.ui.miscellaneous.bitmapDescriptorFromVector
import com.canolabs.rallytransbetxi.ui.results.BottomSheetStageResults
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
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
    val resultsBottomSheetState = rememberModalBottomSheetState()
    val permissionBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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

    val isMapReady = remember { mutableStateOf(false) }
    val startMarkerIcon = remember { mutableStateOf<BitmapDescriptor?>(null) }
    val endMarkerIcon = remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(context) {
        startMarkerIcon.value = BitmapDescriptorFactory.fromBitmap(
            Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.resources, R.drawable.race_start),
                128, 128, false
            )
        )

        endMarkerIcon.value = BitmapDescriptorFactory.fromBitmap(
            Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.resources, R.drawable.race_end),
                128, 128, false
            )
        )
    }

    Column {
        if (state.isLoading || state.isLoadingDirections) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(scaffoldPadding),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else {
            Spacer(modifier = Modifier.padding(scaffoldPadding))
        }

        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                uiSettings = state.uiSettings.copy(
                    mapToolbarEnabled = false,
                ),
                properties = state.mapProperties,
                onMapLoaded = {
                    isMapReady.value = true
                },
                cameraPositionState = cameraPositionState
            ) {

                Polyline(
                    points = state.stage.geoPoints?.map {
                        LatLng(it.latitude, it.longitude)
                    } ?: emptyList(),
                    color = MaterialTheme.colorScheme.primary
                )

                var isStartMarkerInfoWindowVisible by remember { mutableStateOf(false) }
                var isEndMarkerInfoWindowVisible by remember { mutableStateOf(false) }

                // Start point marker
                if (startMarkerIcon.value != null && isMapReady.value) {
                    MarkerInfoWindow(
                        state = MarkerState(state.stage.geoPoints?.first()?.let {
                            LatLng(it.latitude, it.longitude)
                        } ?: betxi),
                        flat = true,
                        icon = startMarkerIcon.value!!,
                        infoWindowAnchor = Offset(x = 0.5f, y = 4f),
                        onInfoWindowClick = {
                            val latLng = state.stage.geoPoints?.first()?.let {
                                LatLng(it.latitude, it.longitude)
                            }
                            val uri = Uri.parse("geo:${latLng?.latitude},${latLng?.longitude}?q=${latLng?.latitude},${latLng?.longitude}")
                            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            context.startActivity(intent)
                        },
                        onClick = {
                            isEndMarkerInfoWindowVisible = false
                            isStartMarkerInfoWindowVisible = !isStartMarkerInfoWindowVisible
                            false
                        }
                    ) {
                        if (isStartMarkerInfoWindowVisible) {
                            CustomInfoWindow(text = state.stage.name.substringBefore("-"))
                        }
                    }
                }

                // End point marker
                if (endMarkerIcon.value != null && isMapReady.value) {
                    MarkerInfoWindow(
                        state = MarkerState(state.stage.geoPoints?.last()?.let {
                            LatLng(it.latitude, it.longitude)
                        } ?: betxi),
                        flat = true,
                        infoWindowAnchor = Offset(x = 0.5f, y = 4f),
                        onInfoWindowClick = {
                            val latLng = state.stage.geoPoints?.first()?.let {
                                LatLng(it.latitude, it.longitude)
                            }
                            val uri = Uri.parse("geo:${latLng?.latitude},${latLng?.longitude}?q=${latLng?.latitude},${latLng?.longitude}")
                            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            context.startActivity(intent)
                        },
                        onClick = {
                            isStartMarkerInfoWindowVisible = false
                            isEndMarkerInfoWindowVisible = !isEndMarkerInfoWindowVisible
                            false
                        },
                        icon = endMarkerIcon.value!!
                    ) {
                        if (isEndMarkerInfoWindowVisible) {
                            CustomInfoWindow(text = state.stage.name.substringAfter("-"))
                        }
                    }
                }

                PrintUserLocation(location = state.location)
                PrintDirections(directions = state.directions)

                HandleCameraAnimations(
                    state = state,
                    cameraPositionState = cameraPositionState,
                    betxi = betxi
                )
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
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            var expanded by remember { mutableStateOf(false) }

            Surface(
                shadowElevation = 4.dp,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
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
                                val mode =
                                    if (state.directionsProfile == DirectionsProfile.FOOT_WALKING) "w" else "d"  // 'w' for walking, 'd' for driving
                                val gmmIntentUri =
                                    Uri.parse("google.navigation:q=${state.directions.last()[1]},${state.directions.last()[0]}&mode=$mode")
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
                                mapsViewModel.setHasPressedDirectionsButton(true)
                                if (state.locationPermissionIsGranted) {
                                    mapsViewModel.getDirections()
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
                sheetState = resultsBottomSheetState,
                onDismissRequest = {
                    coroutineScope.launch {
                        mapsViewModel.setIsResultsBottomSheetVisible(false)
                        resultsBottomSheetState.hide()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                dragHandle = {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 4.dp)
                ) {
                    // Centered Drag Handle
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        BottomSheetDefaults.DragHandle()
                    }

                    // Close button aligned to the end (top-right corner)
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                resultsBottomSheetState.hide()
                                mapsViewModel.setIsResultsBottomSheetVisible(false)
                            }
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                BottomSheetStageResults(
                    resultsState = resultsState,
                    mapsState = state,
                    viewModel = resultsViewModel,
                    isComingFromMaps = true,
                    bottomSheetState = resultsBottomSheetState,
                    navController = navController
                )
            }
        } else if (state.isPermissionDeniedBottomSheetVisible) {
            ModalBottomSheet(
                sheetState = permissionBottomSheetState,
                dragHandle = {},
                onDismissRequest = {
                    coroutineScope.launch {
                        mapsViewModel.setIsPermissionDeniedBottomSheetVisible(false)
                        permissionBottomSheetState.hide()
                    }
                },
            ) {
                BottomSheetPermissionDenied(
                    onOmitButtonPressed = {
                        coroutineScope.launch {
                            mapsViewModel.setIsPermissionDeniedBottomSheetVisible(false)
                            permissionBottomSheetState.hide()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomInfoWindow(
    text: String,
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shape = RoundedCornerShape(32.dp)
            )
            .padding(16.dp)
            .wrapContentSize()
            .clip(RoundedCornerShape(32.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontFamily = robotoFamily,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                // Already handled in the MarkerInfoWindow
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(50)),
            contentPadding = PaddingValues(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_maps_icon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.open_in_google_maps),
                modifier = Modifier.basicMarquee(initialDelayMillis = 1000),
                fontFamily = robotoFamily,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun HandleCameraAnimations(
    state: MapsScreenUIState,
    cameraPositionState: CameraPositionState,
    betxi: LatLng
) {
    when {
        state.directions.isNotEmpty() -> {
            AnimateCameraToDirections(
                directions = state.directions,
                cameraPositionState = cameraPositionState
            )
        }
        state.location != null -> {
            AnimateCameraToUserLocation(
                location = state.location,
                cameraPositionState = cameraPositionState
            )
        }
        state.stage.geoPoints == null -> {
            AnimateCameraToBetxi(betxi = betxi, cameraPositionState = cameraPositionState)
        }
        else -> {
            AnimateCameraToFirstStagePoint(
                stage = state.stage,
                cameraPositionState = cameraPositionState
            )
        }
    }
}

@Composable
fun AnimateCameraToBetxi(
    betxi: LatLng,
    cameraPositionState: CameraPositionState
) {
    val targetPosition = LatLng(betxi.latitude, betxi.longitude)
    LaunchedEffect(targetPosition) {
        delay(2000)
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(targetPosition, 15f),
            durationMs = 2000
        )
    }
}

@Composable
fun AnimateCameraToFirstStagePoint(
    stage: Stage,
    cameraPositionState: CameraPositionState
) {
    val firstPoint = stage.geoPoints?.firstOrNull()?.let {
        LatLng(it.latitude, it.longitude)
    }
    if (firstPoint != null) {
        LaunchedEffect(firstPoint) {
            delay(2000)
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(firstPoint, 15f),
                durationMs = 2000
            )
        }
    }
}

@Composable
fun AnimateCameraToDirections(
    directions: List<List<Double>>,
    cameraPositionState: CameraPositionState
) {
    if (directions.isNotEmpty()) {
        LaunchedEffect(directions) {
            delay(500)
            val (zoomLevel, center) = calculateZoomLevel(directions)
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(center, zoomLevel),
                durationMs = 2000
            )
        }
    }
}

@Composable
fun AnimateCameraToUserLocation(
    location: Location?,
    cameraPositionState: CameraPositionState
) {
    val userPosition = location?.let {
        LatLng(it.latitude, it.longitude)
    }
    if (userPosition != null) {
        LaunchedEffect(userPosition) {
            delay(500)
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(userPosition, 15f),
                durationMs = 2000
            )
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
fun PrintUserLocation(
    location: Location?,
) {
    val context = LocalContext.current
    val icon = remember {
        context.bitmapDescriptorFromVector(R.drawable.location_circle, 0.7f)
    }

    val markerState = remember { MarkerState() }

    // Update marker state only when location changes
    LaunchedEffect(location) {
        location?.let {
            markerState.position = LatLng(it.latitude, it.longitude)
        }
    }

    Marker(
        state = markerState,
        icon = icon,
        onClick = { true }
    )
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