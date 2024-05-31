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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_NEWS
import com.canolabs.rallytransbetxi.utils.DateTimeUtils.secondsToDateInSpanish
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.Locale

@Composable
fun BreakingNewsSection(
    state: RallyScreenUIState,
    viewModel: RallyScreenViewModel,
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = cardsElevation,
        onClick = { viewModel.toggleBreakingNews() }
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
                        .padding(start = 12.dp)
                        .weight(5f)
                )
                IconButton(onClick = { viewModel.toggleBreakingNews() }) {
                    if (!state.areBreakingNewsCollapsed) {
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

            if (!state.isLoading) {
                AnimatedVisibility(visible = state.areBreakingNewsCollapsed.not()) {
                    Column {
                        val newsToShow =
                            if (state.isShowAllBreakingNewsEnabled) state.news
                            else state.news.take(DEFAULT_NEWS)

                        newsToShow.forEach { news ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                onClick = {
                                    navController.navigate("newsDetail/${news.number}")
                                },
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(color = MaterialTheme.colorScheme.errorContainer)
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                ) {
                                    val newsImagePath = news.imageName

                                    val storage = Firebase.storage
                                    val newsStorageRef =
                                        storage.reference.child("${Constants.NEWS_FOLDER}${newsImagePath}")

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

                                    Text(
                                        text = secondsToDateInSpanish(news.date?.seconds ?: 0),
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

                        if (state.news.size > DEFAULT_NEWS) {
                            ClickableText(
                                text = AnnotatedString(
                                    if (state.isShowAllActivitiesEnabled) stringResource(id = R.string.show_less)
                                    else stringResource(id = R.string.show_all)
                                ),
                                onClick = { viewModel.toggleShowAllBreakingNews() },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary),
                            )
                        }
                    }
                }
            }
        }
    }
}