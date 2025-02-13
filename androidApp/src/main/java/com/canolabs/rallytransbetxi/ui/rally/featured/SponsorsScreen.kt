package com.canolabs.rallytransbetxi.ui.rally.featured

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.Constants.Companion.SPONSORS_IMAGE_EXTENSION
import com.canolabs.rallytransbetxi.utils.Constants.Companion.SPONSORS_IMAGE_PREFIX
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SponsorsScreen(
    viewModel: RallyScreenViewModel,
    onBackClick: () -> Unit
) {
    val storage = Firebase.storage
    val state = viewModel.state.collectAsState()
    val sponsorImageUrls = remember { mutableStateListOf<String?>() }
    val errorDuringInitialization = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchNumberOfSponsors()
    }

    LaunchedEffect(state.value.numberOfSponsors) {
        try {
            val totalSponsors = state.value.numberOfSponsors
            if (totalSponsors > 0) {
                for (i in 1..totalSponsors) {
                    val sponsorImagePath = "${SPONSORS_IMAGE_PREFIX}$i${SPONSORS_IMAGE_EXTENSION}"
                    val sponsorStorageRef =
                        storage.reference("${Constants.SPONSORS_FOLDER}$sponsorImagePath")

                    val sponsorUrl = sponsorStorageRef.getDownloadUrl()
                    sponsorImageUrls.add(sponsorUrl)
                    println("SponsorsScreen: Sponsor Image URL: $sponsorUrl")
                }
            }
        } catch (e: Exception) {
            errorDuringInitialization.value = true
            println("SponsorsScreen: Error: $e")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.sponsors),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = ezraFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            modifier = Modifier.size(32.dp),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (sponsorImageUrls.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(sponsorImageUrls) { imageUrl ->
                        SponsorImage(imageUrl = imageUrl)
                    }
                }
            } else if (errorDuringInitialization.value) {
                // Show error state
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.sponsors_not_available),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Show loading state
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

@Composable
fun SponsorImage(imageUrl: String?) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )

    when (painter.state) {
        is AsyncImagePainter.State.Loading,
        is AsyncImagePainter.State.Empty -> {
            Shimmer {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(16.dp)
                        .background(brush = it)
                )
            }
        }

        is AsyncImagePainter.State.Error -> {
            // TODO: Introduce fallback sponsor image
            /*Icon(
                painter = painterResource(id = R.drawable.spo),
                contentDescription = "Error",
                modifier = Modifier
                    .size(48.dp)
                    .align(androidx.compose.ui.Alignment.Center),
                tint = MaterialTheme.colorScheme.error
            )*/
        }

        else -> {
            Image(
                painter = painter,
                contentDescription = "Sponsor Image",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}