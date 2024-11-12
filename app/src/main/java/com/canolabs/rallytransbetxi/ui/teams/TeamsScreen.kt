package com.canolabs.rallytransbetxi.ui.teams

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.results.RacingCategorySegmentedButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    viewModel: TeamsScreenViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Log.d("TeamsScreen", "Fetching teams")
        viewModel.fetchTeams()
    }

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            delay(1500)
            viewModel.fetchTeams()
            pullRefreshState.endRefresh()
        }
    }

    Log.d("TeamsScreen", "Teams: ${state.teams} Size: ${state.teams.size}")

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            TeamsScreenHeader(viewModel = viewModel)

            RacingCategorySegmentedButton(
                selectedRacingCategories = state.selectedRacingCategories,
                onSelectedTabIndexChange = { tabIndex ->
                    if (state.selectedRacingCategories.any { it.getTabIndex() == tabIndex }) {
                        viewModel.removeSelectedRacingCategoryWithIndex(tabIndex)
                    } else {
                        viewModel.addSelectedRacingCategoryWithIndex(tabIndex)
                    }
                }
            )

            TeamsContent(
                teams = state.teams,
                isLoading = state.isLoading,
                state = state,
                navController = navController,
            )
        }

        if (scrollState.value > 100) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp), // Ensure it's not part of the scrollable content
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_upward),
                        contentDescription = "Scroll to top",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullRefreshState,
        )
    }
}