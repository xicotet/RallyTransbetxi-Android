package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.canolabs.rallytransbetxi.data.models.responses.Result


@Composable
fun GlobalResultsTab(
    results: List<Result>,
    isLoading: Boolean,
    state: ResultsScreenUIState
) {
    if (isLoading) {
        for (i in 0..3) {
            ResultsCardShimmer()
        }
    } else {
        val filteredResultsByCategory = results.filter { result ->
            state.selectedRacingCategories.any { selectedCategory ->
                result.team.category.name == stringResource(id = selectedCategory.getName())
            }
        }

        val sortedResultsByTime = filteredResultsByCategory.sortedBy { it.time }

        val filteredResultsBySearchBar = if (state.isSearchBarVisible) {
            sortedResultsByTime.filter { result ->
                result.team.driver.contains(state.searchText, ignoreCase = true) ||
                    result.team.codriver.contains(state.searchText, ignoreCase = true) ||
                    result.team.name.contains(state.searchText, ignoreCase = true) ||
                    result.team.number.contains(state.searchText, ignoreCase = true) ||
                    result.time.contains(state.searchText, ignoreCase = true)
            }
        } else {
            sortedResultsByTime
        }

        sortedResultsByTime.forEachIndexed { index, result ->
            if (filteredResultsBySearchBar.contains(result)) {
                ResultCard(result = result, position = index + 1)
            }
        }
    }
}