package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory


@Composable
fun GlobalResultsTab(
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