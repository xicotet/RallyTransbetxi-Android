package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.ExperimentalFoundationApi
import com.canolabs.rallytransbetxi.data.models.responses.Result
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
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
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
                    selected = (index == state.selectedTabIndex),
                )
            }
        }

        if (state.selectedTabIndex == 0) {
            RacingCategorySegmentedButton(
                selectedTabIndex = state.selectedRacingCategory.getTabIndex(),
                onSelectedTabIndexChange = { viewModel.setSelectedRacingCategory(it) }
            )
            GlobalResultsList(
                results = state.results,
                isLoading = state.isLoading,
                selectedRacingCategory = state.selectedRacingCategory,
                state = state
            )
        } else {
            //StagesResultsList()
        }
    }
}

@Composable
fun GlobalResultsList(
    results: List<Result>,
    isLoading: Boolean,
    selectedRacingCategory: RacingCategory,
    state: ResultsScreenUIState
) {
    if (isLoading) {
        ResultsCardShimmer()
    } else {
        val filteredResultsBySearchBar = if (state.isSearchBarVisible) {
            results.filter { result ->
                result.team.driver.contains(state.searchText, ignoreCase = true) ||
                    result.team.codriver.contains(state.searchText, ignoreCase = true) ||
                    result.team.name.contains(state.searchText, ignoreCase = true) ||
                    result.time.contains(state.searchText, ignoreCase = true)
            }
        } else {
            results
        }

        val filteredResultsByCategory = filteredResultsBySearchBar.filter {
            it.team.category.name == stringResource(id = selectedRacingCategory.getName())
        }

        val sortedResults = filteredResultsByCategory.sortedBy { it.time }

        // We can not use LazyColumn here because we have set up
        // a vertical scrollable component in main function
        sortedResults.forEachIndexed { index, result ->
            ResultCard(result = result, position = index + 1)
        }
    }
}

@Composable
fun ResultsTab(
    title: String,
    onClick: () -> Unit,
    selected: Boolean,
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