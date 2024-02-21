package com.canolabs.rallytransbetxi.ui.navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.canolabs.rallytransbetxi.ui.rally.RallyScreen
import com.canolabs.rallytransbetxi.ui.results.ResultsScreen
import com.canolabs.rallytransbetxi.ui.stages.StagesScreen
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreen

@Composable
fun Navigation(
    stagesScreenViewModel: StagesScreenViewModel
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
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            val iconResource =
                                if (currentRoute == screen.route) screen.iconSelected else screen.iconUnselected
                            Icon(painterResource(id = iconResource), contentDescription = null)
                        },
                        label = { Text(text = screen.title) },
                        selected = currentRoute == screen.route,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.inversePrimary,
                        ),
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
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screens.Rally.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Rally.route) { RallyScreen() }
            composable(Screens.Stages.route) {
                StagesScreen(viewModel = stagesScreenViewModel)
            }
            composable(Screens.Results.route) { ResultsScreen() }
            composable(Screens.Teams.route) { TeamsScreen() }
        }
    }
}