package com.canolabs.rallytransbetxi.ui.teams

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.canolabs.rallytransbetxi.ui.results.RacingCategorySegmentedButton

@Composable
fun TeamsScreen(
    viewModel: TeamsScreenViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("TeamsScreen", "Fetching teams")
        viewModel.fetchTeams()
    }

    Log.d("TeamsScreen", "Teams: ${state.teams} Size: ${state.teams.size}")

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(rememberScrollState())
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
            state = state
        )
    }
}