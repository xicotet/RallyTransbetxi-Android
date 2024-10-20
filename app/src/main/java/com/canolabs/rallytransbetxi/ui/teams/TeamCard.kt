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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
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
        val driverImagePath = "${Constants.DRIVER_IMAGE_PREFIX}${teamNumber}${Constants.DRIVER_IMAGE_EXTENSION}"
        val codriverImagePath = "${Constants.CODRIVER_IMAGE_PREFIX}${teamNumber}${Constants.DRIVER_IMAGE_EXTENSION}"

        val storage = Firebase.storage
        val driverStorageRef = storage.reference.child("${Constants.DRIVERS_FOLDER}${driverImagePath}")
        val codriverStorageRef = storage.reference.child("${Constants.DRIVERS_FOLDER}${codriverImagePath}")

        val driverImageUrl = remember { mutableStateOf<String?>(null) }
        val codriverImageUrl = remember { mutableStateOf<String?>(null) }

        val firstImageIsLoaded = remember { mutableStateOf(false) }
        val secondImageIsLoaded = remember { mutableStateOf(false) }

        LaunchedEffect(teamNumber) {
            try {
                val driverUrl = driverStorageRef.downloadUrl.await()
                driverImageUrl.value = driverUrl.toString()

                val codriverUrl = codriverStorageRef.downloadUrl.await()
                codriverImageUrl.value = codriverUrl.toString()
            } catch (e: Exception) {
                Log.d("DriverImagesPager", "Error: $e")
            }
        }

        val driverPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(driverImageUrl.value ?: "")
                .size(Size.ORIGINAL)
                .build(),
            onState = { state ->
                if (state is AsyncImagePainter.State.Success) {
                    firstImageIsLoaded.value = true
                } else if (state is AsyncImagePainter.State.Loading) {
                    firstImageIsLoaded.value = false
                }
            }
        )

        val codriverPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(codriverImageUrl.value ?: "")
                .size(Size.ORIGINAL)
                .build(),
            onState = { state ->
                if (state is AsyncImagePainter.State.Success) {
                    secondImageIsLoaded.value = true
                } else if (state is AsyncImagePainter.State.Loading) {
                    secondImageIsLoaded.value = false
                }
            }
        )

        val gradient = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
            ),
            start = Offset(0f, 0f), // Start at the top left corner
            end = Offset(1000f, 1000f)
        )

        if (firstImageIsLoaded.value && secondImageIsLoaded.value) {
            Column(
                modifier = Modifier
                    .background(brush = gradient)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
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
                            text = "#" + team.number,
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
                            .height(144.dp)
                            .width(144.dp)
                    )
                    Image(
                        painter = codriverPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(144.dp)
                            .width(144.dp)
                    )
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
        } else {
            Shimmer { brush ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(brush = brush)
                    )

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(48.dp)
                                .background(brush = brush)
                        )
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(144.dp)
                                .height(48.dp)
                                .background(brush = brush)
                        )
                    }
                }
            }
        }
    }
}