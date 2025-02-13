package com.canolabs.rallytransbetxi.ui.results

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.canolabs.rallytransbetxi.R
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage

@Composable
fun DriverImagesPager(result: Result) {

    val teamNumber = result.team.number
    val driverImagePath = "${DRIVER_IMAGE_PREFIX}${teamNumber}${DRIVER_IMAGE_EXTENSION}"
    val codriverImagePath = "${CODRIVER_IMAGE_PREFIX}${teamNumber}${DRIVER_IMAGE_EXTENSION}"

    val storage = Firebase.storage
    val driverStorageRef = storage.reference("${DRIVERS_FOLDER}${driverImagePath}")
    val codriverStorageRef = storage.reference("${DRIVERS_FOLDER}${codriverImagePath}")

    val driverImageUrl = remember { mutableStateOf<String?>(null) }
    val codriverImageUrl = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(teamNumber) {
        driverImageUrl.value = try {
            driverStorageRef.getDownloadUrl()
        } catch (e: Exception) {
            Log.w("DriverImagesPager", "Error when loading driver image of team $teamNumber: $e")
            ""
        }
    }

    LaunchedEffect(teamNumber) {
        codriverImageUrl.value = try {
            codriverStorageRef.getDownloadUrl()
        } catch (e: Exception) {
            Log.w("DriverImagesPager", "Error when loading codriver image of team $teamNumber: $e")
            ""
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
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
        ) { page ->
            val painter = if (page == 0) driverPainter else codriverPainter

            when (painter.state) {
                is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                    Shimmer { brush ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(96.dp)
                                .background(brush = brush)
                        )
                    }
                }

                is AsyncImagePainter.State.Error -> {
                    if (page == 0 && driverImageUrl.value == null
                        || page == 1 && codriverImageUrl.value == null) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(96.dp)
                                    .background(brush = brush)
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.driver_image_default),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(96.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                else -> {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(96.dp),
                        contentScale = ContentScale.Crop
                    )
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
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.onPrimaryContainer else Color.LightGray
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