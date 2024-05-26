package com.canolabs.rallytransbetxi.ui.maps

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    mapsViewModel: MapsScreenViewModel,
    resultsViewModel: ResultsScreenViewModel,
    onBackClick: () -> Unit,
    stageAcronym: String,
    fastAction: String
) {
    val state by mapsViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        mapsViewModel.fetchStage(stageAcronym)
        mapsViewModel.cleanDirections()
        mapsViewModel.cleanLocation()
    }

    val context = LocalContext.current
    val activity = context as Activity

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            mapsViewModel.setLocationPermissionIsGranted(true)
            mapsViewModel.getLocation()
        } else {
            val isFineLocationPermanentlyDenied =
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            val isCoarseLocationPermanentlyDenied =
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

            if (isFineLocationPermanentlyDenied && isCoarseLocationPermanentlyDenied) {
                mapsViewModel.setIsPermissionDeniedBottomSheetVisible(true)
            }
        }
    }

    // Fast actions from swiping left/right the stages card
    LaunchedEffect(stageAcronym, fastAction) {
        if (fastAction == "getDirections") {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
            )
            if (state.locationPermissionIsGranted) {
                mapsViewModel.setHasPressedDirectionsButton(true)
                mapsViewModel.getDirections()
            }
        } else if (fastAction == "results") {
            mapsViewModel.setIsResultsBottomSheetVisible(true)
        }
    }

    val mapUiSettings = MapUiSettings(
        zoomControlsEnabled = false,
        compassEnabled = false
    )

    mapsViewModel.setUiSettings(mapUiSettings)

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
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(4.dp)
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
        MapContent(
            state = state,
            cameraPositionState = cameraPositionState,
            betxi = betxi,
            permissionLauncher = permissionLauncher,
            stageAcronym = stageAcronym,
            scaffoldPadding = it,
            mapsViewModel = mapsViewModel,
            resultsViewModel = resultsViewModel
        )
    }
}