package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.ui.miscellaneous.removeDiacriticalMarks
import com.canolabs.rallytransbetxi.ui.navigation.Screens
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.DateTimeUtils


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

        if (state.globalResults.isEmpty() && state.stages.isNotEmpty()) {
            // Show a placeholder indicating when the results will be available
            val (startTime, languageCode, countryCode) =
                Triple(
                    state.stages.first().startTime?.seconds ?: 0,
                    state.language?.getLanguageCode() ?: "es",
                    state.language?.getCountryCode() ?: "ES"
                )

            val startDate = DateTimeUtils.secondsToDate(
                seconds = startTime,
                language = languageCode,
                country = countryCode
            )
            val startingHour = DateTimeUtils.formatTimeFromSeconds(startTime)

            Text(
                text = stringResource(id = R.string.global_results_available) + ' ' +
                    startDate + ' ' + stringResource(id = R.string.at_hour) + ' ' +
                    startingHour + '.',
                style = MaterialTheme.typography.titleMedium,
                fontFamily = robotoFamily,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
            )
        }

        if (state.globalResults.isNotEmpty() && filteredResultsBySearchBar.isEmpty()) {
            Text(
                text = stringResource(id = R.string.results_are_hidden),
                style = MaterialTheme.typography.titleMedium,
                fontFamily = robotoFamily,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
            )
        }

        sortedResultsByTime.forEachIndexed { index, result ->
            if (filteredResultsBySearchBar.contains(result)) {
                ResultCard(
                    result = result,
                    position = index + 1,
                    onClick = {
                        navController.navigate(
                            "${Screens.TeamDetail.route}/${result.team.number}"
                        )
                    }
                )
            }
        }
    }
}