package com.canolabs.rallytransbetxi.ui.navigation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.canolabs.rallytransbetxi.ui.maps.MapsScreen
import com.canolabs.rallytransbetxi.ui.maps.MapsScreenViewModel
import com.canolabs.rallytransbetxi.ui.rally.RallyScreen
import com.canolabs.rallytransbetxi.ui.results.ResultsScreen
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.stages.StagesScreen
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreen
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@Composable
fun Navigation(
    stagesScreenViewModel: StagesScreenViewModel,
    teamsScreenViewModel: TeamsScreenViewModel,
    resultsScreenViewModel: ResultsScreenViewModel,
    mapsScreenViewModel: MapsScreenViewModel
) {
    val navController = rememberNavController()
    val screens = listOf(
        Screens.Rally,
        Screens.Stages,
        Screens.Results,
        Screens.Teams
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (shouldDisplayNavigationBar(currentRoute)) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                ) {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            val iconResource =
                                if (currentRoute == screen.route) screen.iconSelected else screen.iconUnselected
                            Icon(painterResource(id = iconResource!!), contentDescription = null)
                        },
                        label = {
                            Text(
                                text = stringResource(id = screen.title!!),
                                fontFamily = robotoFamily
                            )
                        },
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
            startDestination = Screens.Rally.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Rally.route) { RallyScreen() }
            composable(Screens.Stages.route) {
                StagesScreen(
                    viewModel = stagesScreenViewModel,
                    navController = navController
                )
            }
            composable(Screens.Results.route) {
                ResultsScreen(viewModel = resultsScreenViewModel)
            }
            composable(Screens.Teams.route) {
                TeamsScreen(viewModel = teamsScreenViewModel)
            }
            composable(
                route = "${Screens.Maps.route}/{stageAcronym}",
                arguments = listOf(navArgument("stageAcronym") { type = NavType.StringType })
            ) {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                val isVisible = currentRoute?.contains(Screens.Maps.route) ?: false

                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(700)),
                    exit = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(700))
                ) {
                    MapsScreen(
                        viewModel = mapsScreenViewModel,
                        stageAcronym = it.arguments?.getString("stageAcronym") ?: ""
                    )
                }
            }
        }
    }
}