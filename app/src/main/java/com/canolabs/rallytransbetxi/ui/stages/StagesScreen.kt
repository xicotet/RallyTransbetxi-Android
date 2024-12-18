package com.canolabs.rallytransbetxi.ui.stages

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.miscellaneous.removeDiacriticalMarks
import com.canolabs.rallytransbetxi.ui.navigation.Screens
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.DateTimeUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StagesScreen(
    stagesViewModel: StagesScreenViewModel,
    navController: NavController,
    sharedPreferences: SharedPreferences
) {
    val state by stagesViewModel.state.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        stagesViewModel.fetchStages()
        stagesViewModel.fetchLanguage(sharedPreferences)
    }

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            delay(1500)
            stagesViewModel.fetchStages()
            stagesViewModel.fetchLanguage(sharedPreferences)
            pullRefreshState.endRefresh()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                StagesScreenHeader(
                    stagesViewModel = stagesViewModel
                )
                StageList(state, navController)
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullRefreshState,
        )
    }
}

@Composable
fun StageList(
    state: StagesScreenUIState,
    navController: NavController,
) {
    // Filter stages based on search text
    val filteredStagesBySearch = if (state.isSearchBarVisible) {
        state.stages.filter { stage ->
            stage.acronym.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                stage.name.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true)
        }
    } else {
        state.stages
    }

    // Sort and group the filtered stages by date
    val sortedStagesByStartTime = filteredStagesBySearch.sortedBy { it.startTime }
    val groupedStagesByDate = sortedStagesByStartTime.groupBy {
        it.startTime?.let { timestamp ->
            DateTimeUtils.secondsToDate(
                seconds = timestamp.seconds,
                language = state.language?.getLanguageCode() ?: "es",
                country = state.language?.getCountryCode() ?: "ES"
            )
        }
    }

    LazyColumn {
        if (state.isLoading) {
            item {
                Shimmer {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(width = 250.dp, height = 32.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(brush = it)
                    )
                }
            }
            items(5) {
                StageCardShimmer()
            }
        }

        // Iterate over the grouped stages by date
        groupedStagesByDate.entries.forEach { entry ->
            // Only show dates with at least one matching stage
            if (entry.value.isNotEmpty()) {
                item {
                    Text(
                        text = entry.key ?: "",
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = robotoFamily,
                        modifier = Modifier.padding(PaddingRegular),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                items(entry.value) { stage ->
                    StageCard(
                        stage = stage,
                        onStageCardClick = { stageSelected, fastAction ->
                            navController.navigate("${Screens.Maps.route}/${stageSelected.acronym}/$fastAction")
                        },
                    )
                }
            }
        }
    }
}