package com.canolabs.rallytransbetxi.ui.stages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.theme.PaddingHuge
import com.canolabs.rallytransbetxi.ui.theme.PaddingLarge
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.DateTimeUtils

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
    val groupedStagesByDate = sortedStagesByStartTime.groupBy {
        it.startTime?.let { timestamp ->
            DateTimeUtils.secondsToDateInSpanish(timestamp.seconds)
        }
    }

    LazyColumn {
        item {
            Text(
                text = stringResource(id = R.string.stages),
                style = MaterialTheme.typography.displayMedium,
                fontFamily = ezraFamily,
                modifier = Modifier.padding(top = PaddingHuge, start = PaddingRegular, end = PaddingHuge, bottom = PaddingLarge)
            )
        }
        groupedStagesByDate.entries.forEach { entry ->
            item {
                Text(
                    text = entry.key ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    fontFamily = robotoFamily,
                    modifier = Modifier.padding(PaddingRegular),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            items(entry.value) { stage ->
                StageCard(stage = stage)
            }
        }
    }
}