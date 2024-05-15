package com.canolabs.rallytransbetxi.ui.maps

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.results.BottomSheetStageResults
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
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
                    onClick = { /*TODO: Handle MyLocation button click*/ },
                    modifier = Modifier.size(60.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.my_location_unknown),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
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
                    onClick = {  },
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