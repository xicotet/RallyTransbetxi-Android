package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.theme.PaddingSmall

@Composable
fun StagesResultsTab(
    stages: List<Stage>,
    isLoading: Boolean
) {
    Spacer(modifier = Modifier.height(PaddingSmall))

    if (isLoading) {
        StagesResultsCardShimmer()
    } else {
        val sortedStagesByStartTime = stages.sortedBy { it.startTime }
        sortedStagesByStartTime.forEach { stage ->
            StagesResultsCard(stage)
        }
    }
}