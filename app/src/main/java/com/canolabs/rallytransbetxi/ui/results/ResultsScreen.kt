package com.canolabs.rallytransbetxi.ui.results

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.TabRow
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.PaddingMedium
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.PaddingSmall
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
    val isRaceProgressBoxVisible = remember { mutableStateOf(false) }

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

            ResultsScreenHeader(
                viewModel = viewModel,
                isRaceProgressStatusBarVisible = isRaceProgressBoxVisible,
                pagerState = pagerState
            )

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

        RaceProgressBox(
            isVisible = isRaceProgressBoxVisible,
            modifier = Modifier
                .align(Alignment.BottomCenter),
            pagerState = pagerState
        )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RaceProgressBox(
    isVisible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    pagerState: PagerState? = null,
    dragDismissThreshold: Float = 150f
) {
    var dragOffset by remember { mutableFloatStateOf(0f) } // Track the vertical drag offset

    // Reset dragOffset when isVisible changes to true
    LaunchedEffect(isVisible.value) {
        if (isVisible.value) {
            dragOffset = 0f
        }
    }

    val isPagerStateVisible = pagerState?.currentPage == 0 || pagerState == null

    AnimatedVisibility(
        visible = isVisible.value && isPagerStateVisible,
        modifier = modifier
            .padding(horizontal = PaddingRegular, vertical = PaddingSmall)
            .offset { IntOffset(0, dragOffset.toInt()) },
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    MaterialTheme.colorScheme.tertiaryContainer,
                    RoundedCornerShape(16.dp)
                )
                .padding(PaddingMedium)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragStart = { /* Optional: Add drag start logic here */ },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume() // Consume the touch event
                            dragOffset = (dragOffset + dragAmount).coerceAtLeast(0f)
                        },
                        onDragEnd = {
                            if (dragOffset > dragDismissThreshold) {
                                isVisible.value = false // Dismiss the element
                            } else {
                                dragOffset = 0f // Reset position
                            }
                        },
                        onDragCancel = {
                            dragOffset = 0f // Reset position on cancel
                        }
                    )
                }
        ) {
            Text(
                text = "Race Progress Status: Results are confirmed and finally! Race Progress Status: Results are confirmed and finally!",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 16.dp) // Add padding to avoid text touching the edge
            )

            // Close button (cross icon) centered vertically
            IconButton(
                onClick = { isVisible.value = false },
                modifier = Modifier.size(48.dp).align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}