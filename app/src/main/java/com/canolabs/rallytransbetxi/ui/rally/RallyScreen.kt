package com.canolabs.rallytransbetxi.ui.rally

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.rally.dialogs.WarningDialog
import com.canolabs.rallytransbetxi.ui.rally.featured.BottomSheetAppSettings
import com.canolabs.rallytransbetxi.ui.rally.featured.FeaturedSection
import com.canolabs.rallytransbetxi.ui.rally.homeSections.ActivityProgramSection
import com.canolabs.rallytransbetxi.ui.rally.homeSections.BreakingNewsSection
import com.canolabs.rallytransbetxi.ui.rally.homeSections.WarningsSection
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RallyScreen(
    viewModel: RallyScreenViewModel,
    darkThemeState: MutableState<Boolean>,
    fontScaleState: MutableState<Float>,
    navController: NavController,
    changeLocale: (String) -> Unit,
    sharedPreferences: SharedPreferences
) {
    val state by viewModel.state.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullToRefreshState()

    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(Unit) {
        viewModel.fetchNews()
        viewModel.fetchActivities()
        viewModel.fetchWarnings()

        // Get the initial theme state. It will take some ms to initialize
        viewModel.updateInitialThemeState(darkThemeState, isSystemInDarkTheme)
        // Get the initial font size factor. It will take some ms to initialize
        viewModel.updateInitialFontSizeFactor(fontScaleState)
        // Get the initial profile. It will take some ms to initialize
        viewModel.updateInitialProfile()
        // Get the initial state of warnings section (if it's collapsed or not)
        viewModel.fetchAreWarningsCollapsed()
        // Get the initial state of breaking news section (if it's collapsed or not)
        viewModel.fetchAreNewsCollapsed()
        // Get the initial state of activities section (if it's collapsed or not)
        viewModel.fetchAreActivitiesCollapsed()
        // Get the initial language. It will be applied immediately
        viewModel.fetchLanguage(sharedPreferences)
    }

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            delay(1500)
            viewModel.fetchNews()
            viewModel.fetchActivities()
            viewModel.fetchWarnings()
            pullRefreshState.endRefresh()
        }
    }

    Box(Modifier.fillMaxSize().nestedScroll(pullRefreshState.nestedScrollConnection)) {
        LazyColumn(
            verticalArrangement = Arrangement.Center
        ) {
            item {
                if (state.isDialogShowing) {
                    WarningDialog(
                        viewModel = viewModel,
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.transbetxi_header),
                    contentDescription = null
                )

                CountdownTimer(
                    darkThemeState = darkThemeState
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.featured_section).uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = ezraFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 38.dp),
                    textAlign = TextAlign.Start
                )

                FeaturedSection(
                    viewModel = viewModel,
                    navController = navController
                )

                WarningsSection(
                    state = state,
                    viewModel = viewModel,
                )

                BreakingNewsSection(
                    state = state,
                    viewModel = viewModel,
                    navController = navController
                )

                ActivityProgramSection(
                    state = state,
                    viewModel = viewModel
                )

                if (state.isSettingsBottomSheetVisible) {
                    ModalBottomSheet(
                        sheetState = bottomSheetState,
                        onDismissRequest = {
                            coroutineScope.launch {
                                viewModel.setIsSettingsBottomSheetVisible(false)
                                bottomSheetState.hide()
                            }
                        },
                    ) {
                        BottomSheetAppSettings(
                            state = state,
                            viewModel = viewModel,
                            darkThemeState = darkThemeState,
                            fontScaleState = fontScaleState,
                            changeLocale = changeLocale
                        )
                    }
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullRefreshState,
        )
    }
}

@Composable
fun getRallyScreenCardsGradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
        ),
        start = Offset(0f, 0f), // Start at the top left corner
        end = Offset(1000f, 1000f)
    )
}