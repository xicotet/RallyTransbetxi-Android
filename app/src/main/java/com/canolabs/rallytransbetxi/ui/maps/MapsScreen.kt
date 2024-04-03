package com.canolabs.rallytransbetxi.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    viewModel: MapsScreenViewModel,
    onBackClick: () -> Unit,
    stageAcronym: String
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchStage(stageAcronym)
    }

    val mapUiSettings = MapUiSettings(
        zoomControlsEnabled = false,
        compassEnabled = false
    )

    viewModel.setUiSettings(mapUiSettings)

    val betxiLocation = Constants.BETXI_LOCATION.split(",")
    val betxi = LatLng(betxiLocation[0].toDouble(), betxiLocation[1].toDouble())

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(betxi, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    if (state.isLoading) Shimmer { brush ->
                        Box(
                            modifier = Modifier
                                .height(24.dp)
                                .width(96.dp)
                                .background(brush = brush)
                        )
                    }
                    else {
                        Text(
                            text = state.stage.name,
                            fontFamily = ezraFamily,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(4.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer, // Circular shape
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) {
        Column {
            if (state.isLoading) {
                LinearProgressIndicator(
                    // dynamic progress value
                    modifier = Modifier
                        .fillMaxWidth() // fill the width of the parent
                        .padding(it), // add some padding
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

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
                    state = MarkerState(betxi),
                    title = state.stage.name,
                )

                // Animate the camera to the first geo point of the stage
                state.stage.geoPoints?.first()?.let { geoPoint ->
                    val targetPosition = LatLng(geoPoint.latitude, geoPoint.longitude)
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPosition, 15f)
                    LaunchedEffect(cameraUpdate) {
                        delay(500)
                        cameraPositionState.animate(cameraUpdate, durationMs = 2000)
                    }
                }
            }
        }
    }
}