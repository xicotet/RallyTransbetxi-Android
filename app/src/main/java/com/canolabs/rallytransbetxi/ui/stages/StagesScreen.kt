package com.canolabs.rallytransbetxi.ui.stages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canolabs.rallytransbetxi.ui.StagesViewModelFactory

@Composable
fun StagesScreen(
    viewModel: StagesScreenViewModel = viewModel(
        factory = StagesViewModelFactory()
    )
) {
    val state by viewModel.state.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Stages Screens")
    }
}