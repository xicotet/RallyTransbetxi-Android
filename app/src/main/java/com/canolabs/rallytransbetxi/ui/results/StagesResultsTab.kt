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
    isLoading: Boolean,
    state: ResultsScreenUIState
) {
    Spacer(modifier = Modifier.height(PaddingSmall))

    if (isLoading) {
        StagesResultsCardShimmer()
    } else {
        val filteredResultsBySearchBar = if (state.isSearchBarVisible) {
            stages.filter { stage ->
                stage.name.contains(state.searchText, ignoreCase = true) ||
                    stage.acronym.contains(state.searchText, ignoreCase = true)
            }
        } else {
            stages
        }

        val sortedStagesByStartTime = filteredResultsBySearchBar.sortedBy { it.startTime }
        sortedStagesByStartTime.forEach { stage ->
            StagesResultsCard(stage)
        }
    }
}