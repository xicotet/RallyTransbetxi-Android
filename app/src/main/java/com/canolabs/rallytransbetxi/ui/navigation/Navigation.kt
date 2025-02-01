package com.canolabs.rallytransbetxi.ui.navigation

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.canolabs.rallytransbetxi.ui.maps.MapsScreen
import com.canolabs.rallytransbetxi.ui.maps.MapsScreenViewModel
import com.canolabs.rallytransbetxi.ui.miscellaneous.UpdateAppVersionScreen
import com.canolabs.rallytransbetxi.ui.navigation.NavigationTransitions.Companion.enterFromBottomTransition
import com.canolabs.rallytransbetxi.ui.navigation.NavigationTransitions.Companion.enterTransition
import com.canolabs.rallytransbetxi.ui.navigation.NavigationTransitions.Companion.exitToBottomTransition
import com.canolabs.rallytransbetxi.ui.navigation.NavigationTransitions.Companion.exitTransition
import com.canolabs.rallytransbetxi.ui.navigation.NavigationTransitions.Companion.popEnterFromBottomTransition
import com.canolabs.rallytransbetxi.ui.navigation.NavigationTransitions.Companion.popEnterTransition
import com.canolabs.rallytransbetxi.ui.navigation.NavigationTransitions.Companion.popExitToBottomTransition
import com.canolabs.rallytransbetxi.ui.navigation.NavigationTransitions.Companion.popExitTransition
import com.canolabs.rallytransbetxi.ui.onboarding.OnboardingFlow
import com.canolabs.rallytransbetxi.ui.rally.*
import com.canolabs.rallytransbetxi.ui.rally.featured.EatScreen
import com.canolabs.rallytransbetxi.ui.rally.featured.HallOfFameScreen
import com.canolabs.rallytransbetxi.ui.rally.featured.SponsorsScreen
import com.canolabs.rallytransbetxi.ui.rally.homeSections.NewsDetailScreen
import com.canolabs.rallytransbetxi.ui.results.ResultsScreen
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.stages.StagesScreen
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import com.canolabs.rallytransbetxi.ui.teams.TeamDetailScreen
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreen
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@Composable
fun Navigation(
    stagesScreenViewModel: StagesScreenViewModel,
    teamsScreenViewModel: TeamsScreenViewModel,
    resultsScreenViewModel: ResultsScreenViewModel,
    mapsScreenViewModel: MapsScreenViewModel,
    rallyScreenViewModel: RallyScreenViewModel,
    darkThemeState: MutableState<Boolean>,
    fontScaleState: MutableState<Float>,
    changeLocale: (String) -> Unit,
    sharedPreferences: SharedPreferences,
    recomposeNavbar: MutableState<Boolean>
) {
    LaunchedEffect(Unit) {
        rallyScreenViewModel.checkAppVersion()
    }

    val navController = rememberNavController()
    val navBarScreens = listOf(
        Screens.Rally,
        Screens.Stages,
        Screens.Results,
        Screens.Teams
    )

    val finishedOnboarding = remember {
        mutableStateOf(
            sharedPreferences.getBoolean(
                "FinishedOnboarding",
                false
            )
        )
    }

    val blockApp = rallyScreenViewModel.blockApp.collectAsState()

    if (blockApp.value) {
        UpdateAppVersionScreen()
    } else {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val shouldShowNavBar = shouldDisplayNavigationBar(currentRoute)

                AnimatedVisibility(
                    visible = shouldShowNavBar,
                    enter = slideInVertically(
                        initialOffsetY = { it }, // Slide in from bottom
                        animationSpec = tween(durationMillis = 400)
                    ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 3.dp,
                    ) {
                        if (recomposeNavbar.value) {
                            recomposeNavbar.value = false
                        }

                        navBarScreens.forEach { screen ->
                            NavigationBarItem(
                                icon = {
                                    Box(
                                        modifier = Modifier
                                            .clip(
                                                if (currentRoute == screen.route) RoundedCornerShape(
                                                    32
                                                ) else RoundedCornerShape(0.dp)
                                            )
                                            .background(
                                                if (currentRoute == screen.route) MaterialTheme.colorScheme.primaryContainer
                                                else Color.Transparent
                                            )
                                            .padding(vertical = 4.dp, horizontal = 16.dp)
                                    ) {
                                        val iconResource =
                                            if (currentRoute == screen.route) screen.iconSelected else screen.iconUnselected
                                        Icon(
                                            painterResource(id = iconResource!!),
                                            contentDescription = null
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = stringResource(id = screen.title!!),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontFamily = robotoFamily
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent,
                                ),
                                selected = currentRoute == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = if (finishedOnboarding.value.not()) Screens.Onboarding.route else Screens.Rally.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                navBarScreens.forEach { screen ->
                    composable(screen.route) {
                        when (screen) {
                            Screens.Rally -> RallyScreen(
                                viewModel = rallyScreenViewModel,
                                darkThemeState = darkThemeState,
                                fontScaleState = fontScaleState,
                                changeLocale = changeLocale,
                                navController = navController,
                                sharedPreferences = sharedPreferences
                            )

                            Screens.Stages -> StagesScreen(
                                stagesViewModel = stagesScreenViewModel,
                                navController = navController,
                                sharedPreferences = sharedPreferences
                            )

                            Screens.Results -> ResultsScreen(
                                viewModel = resultsScreenViewModel,
                                navController = navController,
                                sharedPreferences = sharedPreferences
                            )

                            Screens.Teams -> TeamsScreen(
                                viewModel = teamsScreenViewModel,
                                navController = navController
                            )

                            else -> {}
                        }
                    }
                }

                composable(
                    route = Screens.Onboarding.route,
                    exitTransition = { popExitTransition },
                ) {
                    OnboardingFlow(
                        finishedOnboarding = finishedOnboarding,
                        sharedPreferences = sharedPreferences,
                        changeLanguage = { language ->
                            changeLocale(language)
                        }
                    )
                }
                composable(
                    route = "${Screens.Maps.route}/{stageAcronym}/{fastAction}",
                    arguments = listOf(
                        navArgument("stageAcronym") { type = NavType.StringType },
                        navArgument("fastAction") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = ""
                        }
                    ),
                    enterTransition = { enterTransition },
                    exitTransition = { exitTransition },
                    popEnterTransition = { popEnterTransition },
                    popExitTransition = { popExitTransition }
                ) {
                    MapsScreen(
                        mapsViewModel = mapsScreenViewModel,
                        resultsViewModel = resultsScreenViewModel,
                        onBackClick = { navController.popBackStack() },
                        darkThemeState = darkThemeState,
                        stageAcronym = it.arguments?.getString("stageAcronym") ?: "",
                        fastAction = it.arguments?.getString("fastAction") ?: "",
                        navController = navController,
                        sharedPreferences = sharedPreferences
                    )
                }
                composable(
                    route = "${Screens.TeamDetail.route}/{teamNumber}",
                    arguments = listOf(
                        navArgument("teamNumber") { type = NavType.StringType },
                    ),
                    enterTransition = { enterTransition },
                    exitTransition = { exitTransition },
                    popEnterTransition = { popEnterTransition },
                    popExitTransition = { popExitTransition }
                ) {
                    TeamDetailScreen(
                        teamNumber = it.arguments?.getString("teamNumber") ?: "",
                        teamsViewModel = teamsScreenViewModel,
                        sharedPreferences = sharedPreferences,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable(
                    route = "${Screens.NewsDetail.route}/{newsNumber}",
                    arguments = listOf(
                        navArgument("newsNumber") { type = NavType.StringType },
                    ),
                    enterTransition = { enterTransition },
                    exitTransition = { exitTransition },
                    popEnterTransition = { popEnterTransition },
                    popExitTransition = { popExitTransition }
                ) {
                    NewsDetailScreen(
                        newsNumber = it.arguments?.getString("newsNumber") ?: "",
                        viewModel = rallyScreenViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screens.HallOfFame.route,
                    enterTransition = { enterFromBottomTransition },
                    exitTransition = { exitToBottomTransition },
                    popEnterTransition = { popEnterFromBottomTransition },
                    popExitTransition = { popExitToBottomTransition }
                ) {
                    HallOfFameScreen(
                        viewModel = rallyScreenViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screens.Sponsors.route,
                    enterTransition = { enterFromBottomTransition },
                    exitTransition = { exitToBottomTransition },
                    popEnterTransition = { popEnterFromBottomTransition },
                    popExitTransition = { popExitToBottomTransition }
                ) {
                    SponsorsScreen(
                        viewModel = rallyScreenViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screens.Eat.route,
                    enterTransition = { enterFromBottomTransition },
                    exitTransition = { exitToBottomTransition },
                    popEnterTransition = { popEnterFromBottomTransition },
                    popExitTransition = { popExitToBottomTransition }
                ) {
                    EatScreen(
                        viewModel = rallyScreenViewModel,
                        onBackClick = { navController.popBackStack() },
                        darkThemeState = darkThemeState
                    )
                }
            }
        }
    }
}