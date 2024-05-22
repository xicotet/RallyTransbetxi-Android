package com.canolabs.rallytransbetxi.ui.teams

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    teamNumber: String,
    teamsViewModel: TeamsScreenViewModel
) {
    val state = teamsViewModel.state.collectAsState()
    val team = state.value.teams.find { it.number == teamNumber }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

    val collapsedFraction = scrollBehavior.state.collapsedFraction
    val fontSize = if (collapsedFraction > 0.5) {
        16.sp // Smaller font size when the AppBar is more than half collapsed
    } else {
        24.sp // Larger font size when the AppBar is less than half collapsed
    }

    val teamImagePath = "teamImage${teamNumber}.jpg"

    val storage = Firebase.storage
    val teamStorageRef = storage.reference.child(teamImagePath)

    val teamImageUrl = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(teamNumber) {
        try {
            val teamUrl = teamStorageRef.downloadUrl.await()
            teamImageUrl.value = teamUrl.toString()
            Log.d("DriverImagesPager", "Driver Image URL: $teamUrl")
        } catch (e: Exception) {
            Log.d("DriverImagesPager", "Error: $e")
        }
    }

    val teamPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(teamImageUrl.value ?: "")
            .size(Size.ORIGINAL)
            .build(),
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                title = {
                    if (collapsedFraction > 0.5) {
                        Text(
                            team?.name ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = fontSize,
                            fontFamily = ezraFamily
                        )
                    } else {
                        Log.d("TeamDetailScreen", "Collapsed fraction: $collapsedFraction, team name: ${team?.name}")
                        Text(
                            team?.name ?: "",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = fontSize,
                            fontFamily = ezraFamily
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {  },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            modifier = Modifier.size(32.dp),
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (collapsedFraction < 0.5)
                Spacer(modifier = Modifier.height(40.dp))
            if (teamPainter.state is AsyncImagePainter.State.Loading ||
                teamImageUrl.value == null) {
                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .clip(RectangleShape)
                            .height(400.dp)
                            .width(300.dp)
                            .background(brush = brush)
                    )
                }
            } else {
                Image(
                    painter = teamPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RectangleShape)
                        .height(400.dp)
                        .width(300.dp),
                )
            }
        }
    }
}