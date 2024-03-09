package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ResultsScreen(
    viewModel: ResultsScreenViewModel
) {
    val state by viewModel.state.collectAsState()
    val titles = listOf(R.string.global, R.string.stages)

    LaunchedEffect(Unit) {
        viewModel.fetchGlobalResults()
        viewModel.fetchStages()
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {

        ResultsScreenHeader(viewModel = viewModel)

        SecondaryTabRow(selectedTabIndex = state.selectedTabIndex) {
            titles.forEachIndexed { index, title ->
                ResultsTab(
                    title = stringResource(id = title),
                    onClick = { viewModel.setSelectedTabIndex(index) },
                )
            }
        }

        if (state.selectedTabIndex == 0) {
            RacingCategorySegmentedButton(
                selectedTabIndex = state.selectedRacingCategory.getTabIndex(),
                onSelectedTabIndexChange = { viewModel.setSelectedRacingCategory(it) }
            )
            GlobalResultsTab(
                results = state.globalResults,
                isLoading = state.isLoading,
                selectedRacingCategory = state.selectedRacingCategory,
                state = state
            )
        } else {
            StagesResultsTab(
                stages = state.stages,
                isLoading = state.isLoading,
                state = state
            )
        }
    }
}

@Composable
fun ResultsTab(
    title: String,
    onClick: () -> Unit,
) {
    Column(
        Modifier
            .clickable { onClick() }
            .padding(10.dp)
            .height(40.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = robotoFamily,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}