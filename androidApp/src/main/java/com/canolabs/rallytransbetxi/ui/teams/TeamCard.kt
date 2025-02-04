package com.canolabs.rallytransbetxi.ui.teams

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.navigation.Screens
import com.canolabs.rallytransbetxi.ui.theme.antaFamily
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

@Composable
fun TeamCard(
    team: Team,
    navController: NavController,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = cardsElevation),
        shape = RoundedCornerShape(8.dp)
    ) {
        val teamNumber = team.number
        val driverImagePath =
            "${Constants.DRIVER_IMAGE_PREFIX}${teamNumber}${Constants.DRIVER_IMAGE_EXTENSION}"
        val codriverImagePath =
            "${Constants.CODRIVER_IMAGE_PREFIX}${teamNumber}${Constants.DRIVER_IMAGE_EXTENSION}"

        val storage = Firebase.storage
        val driverStorageRef =
            storage.reference.child("${Constants.DRIVERS_FOLDER}${driverImagePath}")
        val codriverStorageRef =
            storage.reference.child("${Constants.DRIVERS_FOLDER}${codriverImagePath}")

        val driverImageUrl = remember { mutableStateOf<String?>(null) }
        val codriverImageUrl = remember { mutableStateOf<String?>(null) }

        LaunchedEffect(teamNumber) {
            driverImageUrl.value = try {
                driverStorageRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                Log.w("TeamCard", "Error loading driver image for team $teamNumber: $e")
                ""
            }
        }

        LaunchedEffect(teamNumber) {
            codriverImageUrl.value = try {
                codriverStorageRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                Log.w("TeamCard", "Error loading codriver image for team $teamNumber: $e")
                ""
            }
        }

        val driverPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(driverImageUrl.value)
                .size(Size.ORIGINAL)
                .build(),
            error = painterResource(id = R.drawable.driver_image_default)
        )

        val codriverPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(codriverImageUrl.value)
                .size(Size.ORIGINAL)
                .build(),
            error = painterResource(id = R.drawable.driver_image_default)
        )

        val isLoading = driverPainter.state is AsyncImagePainter.State.Loading ||
            codriverPainter.state is AsyncImagePainter.State.Loading

        val fetchingImageUrl = (driverPainter.state is AsyncImagePainter.State.Error && driverImageUrl.value == null) ||
            (codriverPainter.state is AsyncImagePainter.State.Error && codriverImageUrl.value == null)

        // Stabilize loading state with a slight delay to avoid flicker
        var stableLoadingState by remember { mutableStateOf(true) }
        LaunchedEffect(isLoading, fetchingImageUrl) {
            delay(100)
            stableLoadingState = isLoading || fetchingImageUrl
        }

        val gradient = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
            ),
            start = Offset(0f, 0f), // Start at the top left corner
            end = Offset(1000f, 1000f)
        )

        Column(
            modifier = Modifier
                .background(brush = gradient)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                stableLoadingState -> {
                    Shimmer { brush ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(24.dp)
                                    .width(50.dp)
                                    .background(brush = brush)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .height(100.dp)
                                    .width(100.dp)
                                    .background(brush = brush)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .height(100.dp)
                                    .width(100.dp)
                                    .background(brush = brush)
                            )
                        }
                    }
                }
                else -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentWidth()
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(2.dp)
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "#$teamNumber",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = ezraFamily,
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp)
                            )
                        }
                        Image(
                            painter = driverPainter,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .weight(1f)
                                .size(width = 144.dp, height = 120.dp),
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = codriverPainter,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .weight(1f)
                                .size(width = 144.dp, height = 120.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Text(
                text = team.name,
                fontFamily = robotoFamily,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .width(200.dp)
                ) {
                    Text(
                        text = team.driver,
                        fontFamily = antaFamily,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    Text(
                        text = team.codriver,
                        fontFamily = antaFamily,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
                ElevatedButton(
                    onClick = { navController.navigate("${Screens.TeamDetail.route}/${team.number}") },
                    shape = CircleShape,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .width(144.dp)
                        .height(48.dp)

                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }
    }
}