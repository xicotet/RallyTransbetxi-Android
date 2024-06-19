package com.canolabs.rallytransbetxi.ui.stages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.navigation.Screens
import com.canolabs.rallytransbetxi.ui.theme.PaddingHuge
import com.canolabs.rallytransbetxi.ui.theme.PaddingLarge
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.DateTimeUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StagesScreen(
    stagesViewModel: StagesScreenViewModel,
    navController: NavController
) {
    val state by stagesViewModel.state.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        stagesViewModel.fetchStages()
        stagesViewModel.fetchLanguageSettings()
    }

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            delay(1500)
            stagesViewModel.fetchStages()
            stagesViewModel.fetchLanguageSettings()
            pullRefreshState.endRefresh()
        }
    }

    Box(Modifier.fillMaxSize().nestedScroll(pullRefreshState.nestedScrollConnection)) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StageList(state, navController)
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
    val sortedStagesByStartTime = state.stages.sortedBy { it.startTime }
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
        item {
            Text(
                text = stringResource(id = R.string.stages),
                style = MaterialTheme.typography.displaySmall,
                fontFamily = ezraFamily,
                modifier = Modifier.padding(
                    top = PaddingHuge,
                    start = PaddingRegular,
                    end = PaddingHuge,
                    bottom = PaddingLarge
                )
            )
        }
        if (state.isLoading) {
            items(5) {
                StageCardShimmer()
            }
        }
        groupedStagesByDate.entries.forEach { entry ->
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