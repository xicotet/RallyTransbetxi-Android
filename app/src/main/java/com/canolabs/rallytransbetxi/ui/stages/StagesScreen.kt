package com.canolabs.rallytransbetxi.ui.stages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.StagesViewModelFactory

@Composable
fun StagesScreen(stagesViewModelFactory: StagesViewModelFactory) {
    val viewModel: StagesScreenViewModel = viewModel(factory = stagesViewModelFactory)

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Stages Screens")
    }
}

@Composable
fun StageList(stages: List<Stage>) {
    LazyColumn {
        items(stages) { stage ->
            StageItem(stage)
        }
    }
}

@Composable
fun StageItem(stage: Stage) {
    Text(text = stage.name)
}