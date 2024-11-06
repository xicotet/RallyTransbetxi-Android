package com.canolabs.rallytransbetxi.ui.rally.homeSections

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
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
import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenUIState
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_NEWS
import com.canolabs.rallytransbetxi.utils.DateTimeUtils.secondsToDate
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
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
        onClick = {
            viewModel.toggleBreakingNews()
            viewModel.insertSettings()
        }
    ) {
        Column(
            modifier = Modifier
                .background(brush = getBreakingNewsCardGradient())
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.campaign_outlined),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = stringResource(id = R.string.breaking_news).uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = ezraFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 12.dp, top = 4.dp)
                        .weight(5f)
                )
                IconButton(
                    onClick = {
                        viewModel.toggleBreakingNews()
                        viewModel.insertSettings()
                    }
                ) {
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
                                colors = CardColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    val newsImagePath = news.imageName

                                    val storage = Firebase.storage
                                    val newsStorageRef =
                                        storage.reference.child("${Constants.NEWS_FOLDER}${newsImagePath}")

                                    val newsImageUrl = remember { mutableStateOf<String?>(null) }

                                    LaunchedEffect(Unit) {
                                        newsImageUrl.value = try {
                                            newsStorageRef.downloadUrl.await().toString()
                                        } catch (e: Exception) {
                                            Log.d("News", "Error: $e")
                                            ""
                                        }
                                    }

                                    val newsPainter = rememberAsyncImagePainter(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(newsImageUrl.value)
                                            .size(Size.ORIGINAL)
                                            .build(),
                                        error = painterResource(id = R.drawable.news_default_image)
                                    )

                                    val isLoading =
                                        newsPainter.state is AsyncImagePainter.State.Loading ||
                                            newsPainter.state is AsyncImagePainter.State.Empty ||
                                            (newsPainter.state is AsyncImagePainter.State.Error && newsImageUrl.value == null)

                                    // Stabilize loading state with a slight delay to avoid flickering
                                    var stableLoadingState by remember { mutableStateOf(true) }
                                    LaunchedEffect(isLoading) {
                                        delay(100)
                                        stableLoadingState = isLoading
                                    }

                                    when {
                                        stableLoadingState -> {
                                            Shimmer { brush ->
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RectangleShape)
                                                        .fillMaxWidth()
                                                        .height(200.dp)
                                                        .background(brush = brush)
                                                )
                                            }
                                        }

                                        else -> {
                                            Image(
                                                painter = newsPainter,
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .clip(RectangleShape)
                                                    .padding(vertical = 8.dp)
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            modifier = Modifier.padding(end = 4.dp),
                                            contentDescription = null
                                        )
                                        Text(
                                            text = secondsToDate(
                                                seconds = news.date?.seconds ?: 0,
                                                language = state.language?.getLanguageCode()
                                                    ?: "es",
                                                country = state.language?.getCountryCode() ?: "ES"
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = robotoFamily,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }

                                    Text(
                                        text = getNewsTitleByLanguage(news, state.language),
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
                                    if (state.isShowAllBreakingNewsEnabled) stringResource(id = R.string.show_less)
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

@Composable
fun getBreakingNewsCardGradient(): Brush {
    return  Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
}

private fun getNewsTitleByLanguage(news: News, language: Language?): String {
    return when (language) {
        Language.SPANISH -> news.titleEs
        Language.CATALAN -> news.titleCa
        Language.ENGLISH -> news.titleEn
        Language.GERMAN -> news.titleDe
        null -> news.titleEs
    }
}