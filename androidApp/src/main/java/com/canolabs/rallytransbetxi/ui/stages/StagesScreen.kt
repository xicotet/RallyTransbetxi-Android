package com.canolabs.rallytransbetxi.ui.stages

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.miscellaneous.removeDiacriticalMarks
import com.canolabs.rallytransbetxi.ui.navigation.Screens
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.DateTimeUtils

@Composable
fun StagesScreen(
    stagesViewModel: StagesScreenViewModel,
    navController: NavController,
    sharedPreferences: SharedPreferences
) {
    val state by stagesViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        stagesViewModel.fetchStages()
        stagesViewModel.fetchLanguage(sharedPreferences)
    }


    Box(
        Modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()) // Make the column scrollable
            ) {
                StagesScreenHeader(
                    stagesViewModel = stagesViewModel
                )
                StageList(state, navController)
            }
        }
    }
}

@Composable
fun StageList(
    state: StagesScreenUIState,
    navController: NavController,
) {
    // Filter stages based on search text
    val filteredStagesBySearch = if (state.isSearchBarVisible) {
        state.stages.filter { stage ->
            stage.acronym.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                stage.name.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true)
        }
    } else {
        state.stages
    }

    // Sort and group the filtered stages by date
    val sortedStagesByStartTime = filteredStagesBySearch.sortedBy { it.startTime?.seconds ?: 0L }
    val groupedStagesByDate = sortedStagesByStartTime.groupBy {
        it.startTime?.let { timestamp ->
            DateTimeUtils.secondsToDate(
                seconds = timestamp.seconds,
                language = state.language?.getLanguageCode() ?: "es",
                country = state.language?.getCountryCode() ?: "ES"
            )
        }
    }

    // For each date group, render the date and stages
    groupedStagesByDate.entries.forEach { entry ->
        // Only show dates with at least one matching stage
        if (entry.value.isNotEmpty()) {
            Text(
                text = entry.key ?: "",
                style = MaterialTheme.typography.labelLarge,
                fontFamily = robotoFamily,
                modifier = Modifier.padding(PaddingRegular),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // Render the Stage cards for each stage in the group
            entry.value.forEach { stage ->
                StageCard(
                    stage = stage,
                    onStageCardClick = { stageSelected, fastAction ->
                        navController.navigate("${Screens.Maps.route}/${stageSelected.acronym}/$fastAction")
                    },
                )
            }
        }
    }

    // Show shimmer loading effect if necessary
    if (state.isLoading) {
        Shimmer {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(width = 250.dp, height = 32.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(brush = it)
            )
        }

        repeat(5) {
            StageCardShimmer()
        }
    }
}