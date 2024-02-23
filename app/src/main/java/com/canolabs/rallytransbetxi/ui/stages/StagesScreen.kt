package com.canolabs.rallytransbetxi.ui.stages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.canolabs.rallytransbetxi.data.models.responses.Stage

@Composable
fun StagesScreen(viewModel: StagesScreenViewModel) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchStages()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        StageList(state.stages)
    }
}

@Composable
fun StageList(stages: List<Stage>) {
    val sortedStagesByStartTime = stages.sortedBy { it.startTime }
    LazyColumn {
        items(sortedStagesByStartTime) { stage ->
            StageCard(stage = stage)
        }
    }
}