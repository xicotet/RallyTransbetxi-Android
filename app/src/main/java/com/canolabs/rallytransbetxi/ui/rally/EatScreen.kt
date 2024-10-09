package com.canolabs.rallytransbetxi.ui.rally

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import com.canolabs.rallytransbetxi.ui.miscellaneous.bitmapDescriptorFromVector
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EatScreen(
    viewModel: RallyScreenViewModel,
    onBackClick: () -> Unit,
    darkThemeState: MutableState<Boolean>,
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRestaurants()
    }

    val betxiLocation = Constants.BETXI_LOCATION.split(",")
    val betxi = LatLng(betxiLocation[0].toDouble(), betxiLocation[1].toDouble())

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(betxi, 15f)
    }

    val mapUiSettings = MapUiSettings(
        zoomControlsEnabled = false,
        compassEnabled = false
    )

    val context = LocalContext.current

    val mapStyleOptions = if (darkThemeState.value) {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.night_map_style)
    } else {
        null
    }

    val mapProperties = MapProperties().copy(
        mapStyleOptions = mapStyleOptions
    )

    val openMapLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { /* handle the result if needed */ }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.where_to_eat),
                        fontFamily = ezraFamily,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )

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
        val showDialog = remember { mutableStateOf(false) }
        val selectedRestaurant = remember { mutableStateOf<Restaurant?>(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                uiSettings = mapUiSettings,
                properties = mapProperties,
                cameraPositionState = cameraPositionState
            ) {
                state.value.restaurants.forEach { restaurant ->
                    val restaurantPosition =
                        LatLng(restaurant.place.latitude, restaurant.place.longitude)
                    val restaurantMarkerState = MarkerState(restaurantPosition)
                    Marker(
                        state = restaurantMarkerState,
                        title = restaurant.name,
                        icon = if (darkThemeState.value) {
                            context.bitmapDescriptorFromVector(
                                R.drawable.restaurant_outlined,
                                1f
                            )
                        } else {
                            context.bitmapDescriptorFromVector(
                                R.drawable.restaurant_outlined_black,
                                1f
                            )
                        },
                        onInfoWindowClick = {
                            selectedRestaurant.value = restaurant
                            showDialog.value = true
                        },
                        onClick = {
                            selectedRestaurant.value = restaurant
                            showDialog.value = true
                            true
                        }
                    )
                }
            }
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(stringResource(id = R.string.open_in_maps)) },
                text = {Text(
                    stringResource(id = R.string.do_you_want_to_open) + " " + (selectedRestaurant.value?.name ?: "")
                        + " " + stringResource(id = R.string.in_google_maps)
                )  },
                confirmButton = {
                    Button(
                        onClick = {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(selectedRestaurant.value?.url))
                            openMapLauncher.launch(intent)
                            showDialog.value = false
                        }
                    ) {
                        Text(stringResource(id = R.string.yes))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog.value = false }
                    ) {
                        Text(stringResource(id = R.string.no))
                    }
                },
            )
        }
    }
}