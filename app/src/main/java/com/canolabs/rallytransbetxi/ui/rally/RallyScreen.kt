package com.canolabs.rallytransbetxi.ui.rally

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.Locale

@Composable
fun RallyScreen(
    viewModel: RallyScreenViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }

    val headerImagePath = "${Constants.HEADER_FOLDER}${Constants.HEADER_IMAGE_PREFIX}${Constants.HEADER_IMAGE_EXTENSION}"

    val storage = Firebase.storage
    val headerStorageRef = storage.reference.child(headerImagePath)

    val headerImageUrl = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val headerUrl = headerStorageRef.downloadUrl.await()
            headerImageUrl.value = headerUrl.toString()

        } catch (e: Exception) {
            Log.d("RallyScreen error. ", "Error: $e")
        }
    }

    val headerPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(headerImageUrl.value ?: "")
            .size(Size.ORIGINAL)
            .build(),
    )

    LazyColumn(
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Image(
                painter = headerPainter,
                contentDescription = null
            )

            CountdownTimer()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.featured_section).uppercase(Locale.ROOT),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = ezraFamily,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Start
            )

            LazyRow {
                item {
                    Column(
                        modifier = Modifier
                            .width(140.dp)
                            .padding(top = 16.dp, start = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .size(120.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(brush = getRallyScreenCardsGradient())
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.settings),
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = ezraFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .width(140.dp)
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .size(120.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(brush = getRallyScreenCardsGradient())
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.trophy_outlined),
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.list_of_champions),
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = ezraFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .width(140.dp)
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .size(120.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(brush = getRallyScreenCardsGradient())
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sponsors),
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.sponsors),
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = ezraFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .width(140.dp)
                            .padding(top = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .size(120.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(brush = getRallyScreenCardsGradient())
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.restaurant_outlined),
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.where_to_eat),
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = ezraFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            BreakingNewsSection(state = state)
            ActivityProgramCard()
        }
    }
}


@Composable
fun ActivityProgramCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {},
        elevation = CardDefaults.cardElevation(cardsElevation)
    ) {

        Row(
            modifier = Modifier
                .background(brush = getRallyScreenCardsGradient())
                .padding(32.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.explore_outlined),
                modifier = Modifier
                    .size(48.dp)
                    .weight(1f)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.activity_program),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = ezraFamily,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(5f)
            )
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward_ios),
                modifier = Modifier.align(Alignment.CenterVertically),
                contentDescription = null
            )
        }
    }
}

@Composable
fun getRallyScreenCardsGradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
        ),
        start = Offset(0f, 0f), // Start at the top left corner
        end = Offset(1000f, 1000f)
    )
}