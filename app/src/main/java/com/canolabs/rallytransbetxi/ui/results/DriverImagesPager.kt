package com.canolabs.rallytransbetxi.ui.results

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.utils.Constants.Companion.CODRIVER_IMAGE_PREFIX
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DRIVERS_FOLDER
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DRIVER_IMAGE_EXTENSION
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DRIVER_IMAGE_PREFIX
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DriverImagesPager(result: Result) {

    val teamNumber = result.team.number
    val driverImagePath = "${DRIVER_IMAGE_PREFIX}${teamNumber}${DRIVER_IMAGE_EXTENSION}"
    val codriverImagePath = "${CODRIVER_IMAGE_PREFIX}${teamNumber}${DRIVER_IMAGE_EXTENSION}"

    val storage = Firebase.storage
    val driverStorageRef = storage.reference.child("${DRIVERS_FOLDER}${driverImagePath}")
    val codriverStorageRef = storage.reference.child("${DRIVERS_FOLDER}${codriverImagePath}")

    val driverImageUrl = remember { mutableStateOf<String?>(null) }
    val codriverImageUrl = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(teamNumber) {
        try {
            val driverUrl = driverStorageRef.downloadUrl.await()
            driverImageUrl.value = driverUrl.toString()
            Log.d("DriverImagesPager", "Driver Image URL: $driverUrl")

            val codriverUrl = codriverStorageRef.downloadUrl.await()
            codriverImageUrl.value = codriverUrl.toString()
            Log.d("DriverImagesPager", "Codriver Image URL: $codriverUrl")
        } catch (e: Exception) {
            Log.d("DriverImagesPager", "Error: $e")
        }
    }

    val driverPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(driverImageUrl.value ?: "")
            .size(Size.ORIGINAL)
            .build(),
    )

    val codriverPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(codriverImageUrl.value ?: "")
            .size(Size.ORIGINAL)
            .build(),
    )

    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = Modifier.padding(end = 16.dp)
    ){
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
        ) {page ->
            when (page) {
                0 -> {
                    if (driverPainter.state is AsyncImagePainter.State.Loading) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .height(96.dp)
                                    .width(96.dp)
                                    .background(brush = brush)
                            )
                        }
                    } else {
                        Image(
                            painter = driverPainter,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(96.dp)
                                .width(96.dp)
                        )
                    }
                }

                1 -> {
                    if (codriverPainter.state is AsyncImagePainter.State.Loading) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .height(96.dp)
                                    .width(96.dp)
                                    .background(brush = brush)
                            )
                        }
                    } else {
                        Image(
                            painter = codriverPainter,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(96.dp)
                                .width(96.dp)
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.onPrimaryContainer
                    else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }
}