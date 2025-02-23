package com.canolabs.rallytransbetxi.ui.rally.featured

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

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
                    val sponsorStorageRef = storage.reference.child("${Constants.SPONSORS_FOLDER}$sponsorImagePath")

                    val sponsorUrl = sponsorStorageRef.downloadUrl.await()
                    sponsorImageUrls.add(sponsorUrl.toString())
                    Log.d("SponsorsScreen", "Sponsor Image URL: $sponsorUrl")
                }
            }
        } catch (e: Exception) {
            errorDuringInitialization.value = true
            Log.d("SponsorsScreen", "Error: $e")
        }
    }

    val sponsorPainters = sponsorImageUrls.map { url ->
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url ?: "")
                .size(Size.ORIGINAL)
                .build(),
        )
    }

    val isLoading = sponsorImageUrls.isEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.sponsors),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ezraFamily
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            modifier = Modifier.size(32.dp),
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
    ) {
        if (errorDuringInitialization.value) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.sponsors_not_available),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        } else {

            if (isLoading) {
                Column {
                    LinearProgressIndicator(
                        // dynamic progress value
                        modifier = Modifier
                            .fillMaxWidth() // fill the width of the parent
                            .padding(it), // add some padding
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sponsorPainters) { painter ->
                    when (painter.state) {
                        is AsyncImagePainter.State.Loading,
                        is AsyncImagePainter.State.Empty -> {
                            Shimmer { brush ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .padding(16.dp)
                                        .background(brush = brush)
                                )
                            }
                        }

                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        else -> {
                            // TODO: Probar si este bloque funciona cuando cambiamos el numero del for de arriba
                            Text(
                                text = stringResource(id = R.string.sponsors_not_available),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}