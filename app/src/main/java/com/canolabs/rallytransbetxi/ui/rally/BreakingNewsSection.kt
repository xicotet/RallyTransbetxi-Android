package com.canolabs.rallytransbetxi.ui.rally

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.DateTimeUtils
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.Locale

@Composable
fun BreakingNewsSection(
    state: RallyScreenUIState
) {
    val isContentVisible = remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(32.dp),
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.errorContainer)
                .padding(16.dp)
        ) {
            Row {
                Text(
                    text = stringResource(id = R.string.breaking_news).uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = ezraFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(5f)
                )
                IconButton(onClick = { isContentVisible.value = !isContentVisible.value }) {
                    if (isContentVisible.value) {
                        Icon(
                            painter = painterResource(id = R.drawable.collapse_all),
                            modifier = Modifier
                                .size(48.dp)
                                .weight(1f)
                                .padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.expand_all),
                            modifier = Modifier
                                .size(48.dp)
                                .weight(1f)
                                .padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isContentVisible.value) {
                if (!state.isLoading) {
                    state.news.forEach { news ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {},
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.errorContainer)
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                            ) {
                                val newsImagePath = news.imageName

                                val storage = Firebase.storage
                                val newsStorageRef = storage.reference.child("${Constants.NEWS_FOLDER}${newsImagePath}")

                                val newsImageUrl = remember { mutableStateOf<String?>(null) }

                                LaunchedEffect(Unit) {
                                    try {
                                        val imageUrl = newsStorageRef.downloadUrl.await()
                                        newsImageUrl.value = imageUrl.toString()
                                    } catch (e: Exception) {
                                        Log.d("News", "Error: $e")
                                    }
                                }

                                val newsPainter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(newsImageUrl.value ?: "")
                                        .size(Size.ORIGINAL)
                                        .build(),
                                )

                                if (newsPainter.state is AsyncImagePainter.State.Loading) {
                                    Shimmer { brush ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RectangleShape)
                                                .fillMaxWidth()
                                                .height(200.dp)
                                                .background(brush = brush)
                                        )
                                    }
                                } else {
                                    Image(
                                        painter = newsPainter,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clip(RectangleShape)
                                            .padding(vertical = 8.dp)
                                            .fillMaxWidth()
                                    )
                                }

                                val newsDateInSeconds = news.date?.seconds ?: 0
                                val daysAndHoursSince =
                                    DateTimeUtils.getDaysAndHoursSince(newsDateInSeconds)
                                val timeSinceNewsDate = when {
                                    daysAndHoursSince.first != "0" && daysAndHoursSince.first != "1" -> {
                                        "${daysAndHoursSince.first} " + stringResource(id = R.string.days)
                                    }

                                    daysAndHoursSince.first == "1" -> {
                                        "${daysAndHoursSince.first} " + stringResource(id = R.string.day)
                                    }

                                    daysAndHoursSince.second == "1" -> {
                                        "${daysAndHoursSince.second} " + stringResource(id = R.string.hour)
                                    }

                                    else -> {
                                        "${daysAndHoursSince.second} " + stringResource(id = R.string.hours)
                                    }
                                }

                                Text(
                                    text = timeSinceNewsDate,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = robotoFamily,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )

                                Text(
                                    text = news.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontFamily = robotoFamily,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}