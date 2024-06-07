package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.ui.miscellaneous.removeDiacriticalMarks


@Composable
fun GlobalResultsTab(
    results: List<Result>,
    isLoading: Boolean,
    state: ResultsScreenUIState,
    navController: NavController
) {
    if (isLoading) {
        for (i in 0..3) {
            ResultsCardShimmer()
        }
    } else {
        val filteredResultsByCategory = results.filter { result ->
            state.selectedRacingCategories.any { selectedCategory ->
                result.team.category.name == selectedCategory.getApiName()
            }
        }

        val sortedResultsByTime = filteredResultsByCategory.sortedBy { it.time }

        val filteredResultsBySearchBar = if (state.isSearchBarVisible) {
            sortedResultsByTime.filter { result ->
                result.team.driver.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    result.team.codriver.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    result.team.name.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    result.team.number.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    result.time.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true)
            }
        } else {
            sortedResultsByTime
        }

        sortedResultsByTime.forEachIndexed { index, result ->
            if (filteredResultsBySearchBar.contains(result)) {
                ResultCard(
                    result = result,
                    position = index + 1,
                    navController = navController
                )
            }
        }
    }
}