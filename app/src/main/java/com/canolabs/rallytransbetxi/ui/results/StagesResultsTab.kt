package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.ui.theme.PaddingSmall
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StagesResultsTab(
    stages: List<Stage>,
    isLoading: Boolean,
    state: ResultsScreenUIState,
    viewModel: ResultsScreenViewModel,
    navController: NavController
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

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
            StagesResultsCard(stage, onClick = {stageSelected ->
                coroutineScope.launch {
                    viewModel.setSelectedStage(stageSelected)
                    viewModel.fetchStagesResults(stageSelected.acronym)
                    viewModel.setIsBottomSheetVisible(true)
                    bottomSheetState.show()
                }
            })
        }
    }

    if (state.isBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    viewModel.setIsBottomSheetVisible(false)
                    bottomSheetState.hide()
                }
            },
        ) {
            BottomSheetStageResults(
                state = state,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}