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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import com.canolabs.rallytransbetxi.ui.theme.antaFamily
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    teamNumber: String,
    teamsViewModel: TeamsScreenViewModel,
    onBackClick: () -> Unit
) {
    val state = teamsViewModel.state.collectAsState()

    val hasFetchedGlobalResult = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        teamsViewModel.fetchTeams()

        teamsViewModel.state.collect { uiState ->
            if (hasFetchedGlobalResult.value) return@collect
            val team = uiState.teams.find { it.number == teamNumber }
            if (team != null) {
                teamsViewModel.fetchGlobalResultOfATeam(team)
                teamsViewModel.fetchStageResultsOfATeam(team)
                hasFetchedGlobalResult.value = true
            }
        }
    }

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

    val teamImagePath =
        "${Constants.TEAM_IMAGE_PREFIX}${teamNumber}${Constants.TEAM_IMAGE_EXTENSION}"

    val storage = Firebase.storage
    val teamStorageRef = storage.reference.child("${Constants.TEAMS_FOLDER}/$teamImagePath")

    val teamImageUrl = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(teamNumber) {
        try {
            val teamUrl = teamStorageRef.downloadUrl.await()
            teamImageUrl.value = teamUrl.toString()
            Log.d("DriverImagesPager", "Driver Image URL: $teamUrl")
        } catch (e: Exception) {
            Log.d("DriverImagesPager", "Error: $e")
        }

        teamsViewModel.fetchGlobalResultOfATeam(team ?: return@LaunchedEffect)
        teamsViewModel.fetchStageResultsOfATeam(team)
    }

    val teamPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(teamImageUrl.value ?: "")
            .size(Size.ORIGINAL)
            .build(),
    )

    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
        ),
        start = Offset(0f, 0f), // Start at the top left corner
        end = Offset(1000f, 1000f)
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                        onClick = { onBackClick() },
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
                .background(brush = gradient)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (teamImageUrl.value == null) {
                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .clip(RectangleShape)
                            .height(400.dp)
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .background(brush = brush)
                    )
                }
            }

            when (teamPainter.state) {
                is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Empty -> {
                    Shimmer { brush ->
                        Box(
                            modifier = Modifier
                                .clip(RectangleShape)
                                .height(400.dp)
                                .width(300.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(brush = brush)
                        )
                    }
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = teamPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RectangleShape)
                            .height(400.dp)
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    // Show a placeholder image
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 48.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(2.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "#" + team?.number,
                        fontSize = 28.sp,
                        fontFamily = ezraFamily,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = team?.category?.name ?: "",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = antaFamily,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )

            }

            val onBackgroundColor = MaterialTheme.colorScheme.onBackground

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .drawBehind {
                        val strokeWidth = 4f
                        val width = size.width
                        val height = size.height

                        val path = Path().apply {
                            // Top-right corner
                            moveTo(width, 0f)
                            lineTo(width - 50f, 0f)
                            lineTo(width - 50f, strokeWidth)
                            lineTo(width, strokeWidth)
                            close()
                            moveTo(width, 0f)
                            lineTo(width, 50f)
                            lineTo(width - strokeWidth, 50f)
                            lineTo(width - strokeWidth, 0f)
                            close()

                            // Bottom-left corner
                            moveTo(0f, height)
                            lineTo(50f, height)
                            lineTo(50f, height - strokeWidth)
                            lineTo(0f, height - strokeWidth)
                            close()
                            moveTo(0f, height)
                            lineTo(0f, height - 50f)
                            lineTo(strokeWidth, height - 50f)
                            lineTo(strokeWidth, height)
                            close()
                        }
                        drawPath(path, onBackgroundColor, style = Stroke(width = strokeWidth))
                    }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    if (state.value.isLoadingCategoryResult) {
                        Shimmer { brush ->
                            Row {
                                Text(
                                    text = stringResource(id = R.string.category_position) + ": ",
                                    fontSize = 20.sp,
                                    fontFamily = ezraFamily,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RectangleShape)
                                        .height(20.dp)
                                        .fillMaxWidth()
                                        .background(brush = brush)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.category_position) + ": " + state.value.categoryResult,
                            fontSize = 20.sp,
                            fontFamily = ezraFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (state.value.isLoadingGlobalResult) {
                        Shimmer { brush ->
                            Row {
                                Text(
                                    text = stringResource(id = R.string.overall_position) + ": ",
                                    fontSize = 20.sp,
                                    fontFamily = ezraFamily,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RectangleShape)
                                        .height(20.dp)
                                        .fillMaxWidth()
                                        .background(brush = brush)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.overall_position) + ": " + state.value.globalResult,
                            fontSize = 20.sp,
                            fontFamily = ezraFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (state.value.isLoadingGlobalTime) {
                        Shimmer { brush ->
                            Row {
                                Text(
                                    text = stringResource(id = R.string.overall_time) + ": ",
                                    fontSize = 20.sp,
                                    fontFamily = ezraFamily,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RectangleShape)
                                        .height(20.dp)
                                        .fillMaxWidth()
                                        .background(brush = brush)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.overall_time) + ": ",
                            fontSize = 20.sp,
                            fontFamily = ezraFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                        Text(
                            text = state.value.globalTime,
                            fontSize = 20.sp,
                            fontFamily = ezraFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            Text(
                text = stringResource(id = R.string.stages),
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp, start = 32.dp, end = 32.dp),
                fontSize = 24.sp,
                fontFamily = ezraFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Row (
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = stringResource(id = R.string.stage_victories),
                        fontSize = 20.sp,
                        fontFamily = ezraFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    if (state.value.isLoadingStageVictories) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .clip(RectangleShape)
                                    .height(20.dp)
                                    .width(80.dp)
                                    .background(brush = brush)
                            )
                        }
                    } else {
                        Text(
                            text = state.value.stageVictories.toString(),
                            fontSize = 32.sp,
                            fontFamily = ezraFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 8.dp)
                        .width(4.dp),
                    thickness = 4.dp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = stringResource(id = R.string.best_stage_position),
                        fontSize = 20.sp,
                        fontFamily = ezraFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    if (state.value.isLoadingBestStagePosition) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .clip(RectangleShape)
                                    .height(20.dp)
                                    .width(80.dp)
                                    .background(brush = brush)
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = state.value.bestStagePosition.toString(),
                                fontSize = 32.sp,
                                fontFamily = ezraFamily,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )
                            if (state.value.numberOfTimesBestPosition > 1) {
                                Text(
                                    text = "x" + state.value.numberOfTimesBestPosition.toString(),
                                    modifier = Modifier.padding(bottom = 6.dp),
                                    fontSize = 16.sp,
                                    fontFamily = ezraFamily,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}