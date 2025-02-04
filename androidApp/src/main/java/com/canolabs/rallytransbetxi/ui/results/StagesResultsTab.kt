package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.theme.PaddingSmall
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StagesResultsTab(
    stages: List<Stage>,
    isLoading: Boolean,
    state: ResultsScreenUIState,
    viewModel: ResultsScreenViewModel,
    navController: NavController,
    language: Language
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(PaddingSmall))

    if (isLoading) {
        for (i in 0..3) {
            StagesResultsCardShimmer()
        }
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
            StagesResultsCard(stage, onClick = { stageSelected ->
                coroutineScope.launch {
                    viewModel.setSelectedStage(stageSelected)
                    viewModel.fetchStagesResults(stageSelected.acronym)
                    viewModel.fetchStageRaceWarning(stageSelected.acronym)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            dragHandle = {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp)
            ) {
                // Centered Drag Handle
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    BottomSheetDefaults.DragHandle()
                }

                // Close button aligned to the end (top-right corner)
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                            viewModel.setIsBottomSheetVisible(false)
                        }
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Main content of the bottom sheet
            BottomSheetStageResults(
                resultsState = state,
                viewModel = viewModel,
                stageRaceWarning = state.raceWarning,
                bottomSheetState = bottomSheetState,
                navController = navController,
                language = language
            )
        }
    }
}