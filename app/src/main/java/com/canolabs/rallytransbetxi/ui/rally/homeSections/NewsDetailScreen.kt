package com.canolabs.rallytransbetxi.ui.rally.homeSections

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.News
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.DateTimeUtils
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

@Composable
fun NewsDetailScreen(
    newsNumber: String,
    viewModel: RallyScreenViewModel,
    onBackClick: () -> Unit
) {
    val state = viewModel.state.collectAsState()

    val news = state.value.news.find {
        it.number == newsNumber
    }!!

    val scrollState = rememberScrollState()

    fun getNewsTitleByLanguage(news: News, language: Language?): String {
        return when (language) {
            Language.SPANISH -> news.titleEs
            Language.CATALAN -> news.titleCa
            Language.ENGLISH -> news.titleEn
            Language.GERMAN -> news.titleDe
            null -> news.titleEs
        }
    }

    fun getNewsContentByLanguage(news: News, language: Language?): String {
        return when (language) {
            Language.SPANISH -> news.contentEs
            Language.CATALAN -> news.contentCa
            Language.ENGLISH -> news.contentEn
            Language.GERMAN -> news.contentDe
            null -> news.contentEs
        }
    }

    Scaffold(
        topBar = {
            NewsDetailTopBar(
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.scrim,
        contentColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
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
                    Log.d("NewsDetail", "Error: $e")
                    ""
                }
            }

            val newsPainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsImageUrl.value ?: "")
                    .size(Size.ORIGINAL)
                    .build(),
            )

            Text(
                text = getNewsTitleByLanguage(news, state.value.language),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = robotoFamily
            )

            when (newsPainter.state) {
                is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Empty -> {
                    Shimmer { brush ->
                        Box(
                            modifier = Modifier
                                .clip(RectangleShape)
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(vertical = 8.dp)
                                .background(brush = brush)
                        )
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    if (newsImageUrl.value == null) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .clip(RectangleShape)
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(vertical = 8.dp)
                                    .background(brush = brush)
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.news_default_image),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .clip(RectangleShape)
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "(" + stringResource(id = R.string.news_image_not_available) + ")",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontFamily = robotoFamily
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

            Row (
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp, bottom = 8.dp)
            ){
                Icon(
                    imageVector = Icons.Default.DateRange,
                    modifier = Modifier.padding(end = 4.dp),
                    contentDescription = null
                )
                Text(
                    text = DateTimeUtils.secondsToDate(
                        seconds = news.date?.seconds ?: 0,
                        language = state.value.language?.getLanguageCode() ?: "es",
                        country = state.value.language?.getCountryCode() ?: "ES"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = robotoFamily,
                    color = Color.White,
                )
            }

            getNewsContentByLanguage(news, state.value.language)
                .split("\\n").forEach { paragraph ->
                    Text(
                        text = paragraph,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontFamily = robotoFamily
                    )
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.scrim,
            titleContentColor = Color.White,
        ),
    )
}