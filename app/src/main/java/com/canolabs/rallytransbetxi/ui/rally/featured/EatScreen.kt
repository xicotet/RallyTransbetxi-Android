package com.canolabs.rallytransbetxi.ui.rally.featured

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import com.canolabs.rallytransbetxi.ui.miscellaneous.bitmapDescriptorFromVector
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EatScreen(
    viewModel: RallyScreenViewModel,
    onBackClick: () -> Unit,
    darkThemeState: MutableState<Boolean>,
) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val selectedRestaurant = remember { mutableStateOf<Restaurant?>(null) }
    val pagerState = rememberPagerState(pageCount = { state.value.restaurants.size })

    val betxiLocation = Constants.BETXI_LOCATION.split(",")
    val betxi = LatLng(betxiLocation[0].toDouble(), betxiLocation[1].toDouble())

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(betxi, 15f)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchRestaurants()
    }

    LaunchedEffect(pagerState.currentPage) {
        if (state.value.restaurants.isNotEmpty() && pagerState.currentPage in state.value.restaurants.indices) {
            selectedRestaurant.value = state.value.restaurants[pagerState.currentPage]
            val restaurantPosition = LatLng(
                state.value.restaurants[pagerState.currentPage].place.latitude,
                state.value.restaurants[pagerState.currentPage].place.longitude
            )

            val currentZoom = cameraPositionState.position.zoom
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(restaurantPosition, currentZoom)
            coroutineScope.launch {
                cameraPositionState.animate(cameraUpdate, durationMs = 1000)
            }
        } else {
            // Handle empty list or out-of-bounds index, for example:
            Log.e("onPageChange", "No restaurants available or invalid index: $pagerState.currentPage")
        }
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
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
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
                            modifier = Modifier.size(32.dp),
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) {
        val showDialog = remember { mutableStateOf(false) }
        val mapLoaded = remember { mutableStateOf(false) }

        Column {
            if (mapLoaded.value.not()) {
                LinearProgressIndicator(
                    // dynamic progress value
                    modifier = Modifier
                        .fillMaxWidth() // fill the width of the parent
                        .padding(it), // add some padding
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    uiSettings = mapUiSettings,
                    properties = mapProperties,
                    onMapLoaded = { mapLoaded.value = true },
                    cameraPositionState = cameraPositionState
                ) {
                    state.value.restaurants.forEach { restaurant ->
                        val restaurantPosition = LatLng(restaurant.place.latitude, restaurant.place.longitude)
                        MarkerInfoWindow(
                            state = MarkerState(position = restaurantPosition),
                            title = restaurant.name,
                            onClick = {
                                selectedRestaurant.value = restaurant
                                // Move the pagerstate to the selected restaurant
                                val index = state.value.restaurants.indexOf(restaurant)
                                coroutineScope.launch {
                                    pagerState.scrollToPage(index)
                                }
                                true
                            },
                            content = {},
                            zIndex = if (restaurant == selectedRestaurant.value) 1f else 0f,
                            icon = if (restaurant == selectedRestaurant.value) createCustomRestaurantMarkerWithRating(LocalContext.current, 4.5f)
                                else LocalContext.current.bitmapDescriptorFromVector(R.drawable.resturant_red_background, 1f),
                        )
                    }
                }

                // Restaurant Cards
                RestaurantCards(
                    restaurants = state.value.restaurants,
                    onCardClick = { restaurant ->
                        selectedRestaurant.value = restaurant
                        showDialog.value = true
                    },
                    pagerState = pagerState,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp)
                )
            }
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(stringResource(id = R.string.open_in_maps)) },
                text = {
                    Text(
                        stringResource(id = R.string.do_you_want_to_open) + " " + (selectedRestaurant.value?.name
                            ?: "")
                            + " " + stringResource(id = R.string.in_google_maps)
                    )
                },
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

fun createCustomRestaurantMarkerWithRating(context: Context, rating: Float): BitmapDescriptor {
    val circleDiameter = 100  // Diameter of the circle (left side)
    val rectWidth = 120       // Width of the rectangle (right side)
    val height = 120          // Increased height for both the circle and the rectangle (to accommodate padding)

    // Padding to be added to the left, top, bottom, and right of the elements
    val padding = 12f // Padding to create space around the drawable and text
    val topBottomPadding = 8f // Padding to add on top and bottom of the drawable

    // Create a Bitmap with the total width (circle + rectangle) and height
    val bitmap = Bitmap.createBitmap(circleDiameter + rectWidth, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Draw the rectangle (background) first with a larger corner radius
    val rectPaint = Paint().apply {
        color = Color.parseColor("#EA4336") // Set the rectangle color
        isAntiAlias = true
    }
    val rectLeft = 0f
    val rectTop = 0f
    val rectRight = rectLeft + circleDiameter + rectWidth
    val rectBottom = height.toFloat()

    // Increase the corner radius to make both left and right sides rounded
    val cornerRadius = height / 2f // Set the corner radius to be the same as the height for a circular effect
    canvas.drawRoundRect(rectLeft, rectTop, rectRight, rectBottom, cornerRadius, cornerRadius, rectPaint)

    // Load the restaurant_white_background drawable and convert it to Bitmap
    val drawable = ContextCompat.getDrawable(context, R.drawable.restaurant_white_background) as VectorDrawable
    val circleBitmap = Bitmap.createBitmap(circleDiameter,
        (height - topBottomPadding * 2).toInt(), Bitmap.Config.ARGB_8888) // Adjusted height
    val circleCanvas = Canvas(circleBitmap)
    drawable.setBounds(0, 0, circleDiameter, circleBitmap.height)
    drawable.draw(circleCanvas)

    // Add padding to the circle: Draw the circle on the left part of the rectangle with some padding
    canvas.drawBitmap(circleBitmap, padding, topBottomPadding, null)

    // Draw the restaurant rating on the right side of the rectangle with padding
    val typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
    val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 50f // Moderate text size
        textAlign = Paint.Align.LEFT // Align text to the left side of the rectangle
        isAntiAlias = true
        this.typeface = typeface  // Set the custom font
    }

    // Add padding between the rating text and the circle
    val textPadding = 10f
    val ratingStartX = circleDiameter + textPadding + padding // Start drawing text after the circle and padding

    // Calculate the vertical center for the text
    val textHeight = textPaint.fontMetrics.bottom - textPaint.fontMetrics.top
    val ratingStartY = (height / 2f) + (textHeight / 2f) - 20f // Adjust by half of the text height to center it

    // Draw the rating text centered vertically
    canvas.drawText("$rating", ratingStartX, ratingStartY, textPaint)

    // Return the BitmapDescriptor for the custom marker
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}