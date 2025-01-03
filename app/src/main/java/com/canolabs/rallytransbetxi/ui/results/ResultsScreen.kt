package com.canolabs.rallytransbetxi.ui.results

import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TabRow
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: ResultsScreenViewModel,
    navController: NavController,
    sharedPreferences: SharedPreferences
) {
    val state by viewModel.state.collectAsState()
    val titles = listOf(R.string.global, R.string.stages)
    val pagerState = rememberPagerState(pageCount = { titles.size })
    val coroutineScope = rememberCoroutineScope()

    // Remember the refreshing state
    val pullRefreshState = rememberPullToRefreshState()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.fetchGlobalResults()
        viewModel.fetchStages()
        viewModel.fetchLanguage(sharedPreferences)
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            delay(1500)
            viewModel.fetchGlobalResults()
            viewModel.fetchStages()
            viewModel.fetchLanguage(sharedPreferences)
            pullRefreshState.endRefresh()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            ResultsScreenHeader(viewModel = viewModel)

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                titles.forEachIndexed { index, title ->
                    ResultsTab(
                        title = stringResource(id = title),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
            ) { page ->
                when (page) {
                    0 -> {
                        Column(modifier = Modifier.defaultMinSize(minHeight = screenHeight)) {
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
                            GlobalResultsTab(
                                results = state.globalResults,
                                isLoading = state.isLoading,
                                state = state,
                                navController = navController
                            )
                        }
                    }

                    1 -> {
                        Column(
                            modifier = Modifier.defaultMinSize(minHeight = screenHeight)
                        ) {
                            StagesResultsTab(
                                stages = state.stages,
                                isLoading = state.isLoading,
                                state = state,
                                viewModel = viewModel,
                                navController = navController
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullRefreshState,
        )

        if (scrollState.value > 100 && pagerState.currentPage == 0) {
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
    }
}

@Composable
fun ResultsTab(
    title: String,
    onClick: () -> Unit,
) {
    Column(
        Modifier
            .clickable { onClick() }
            .padding(10.dp)
            .height(40.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = robotoFamily,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}