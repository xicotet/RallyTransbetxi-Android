package com.canolabs.rallytransbetxi.ui.teams

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Teams Screens")
    }
}