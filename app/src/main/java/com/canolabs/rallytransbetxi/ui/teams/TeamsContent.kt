package com.canolabs.rallytransbetxi.ui.teams

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.ui.miscellaneous.removeDiacriticalMarks

@Composable
fun TeamsContent(
    teams: List<Team>,
    isLoading: Boolean,
    state: TeamsScreenUIState,
    navController: NavController,
) {
    if (isLoading) {
        TeamCardShimmer()
        TeamCardShimmer()
    } else {

        val sortedTeamsByNumber = teams.sortedBy { it.number.toIntOrNull() ?: Int.MAX_VALUE }

        val filteredTeamsByCategory = sortedTeamsByNumber.filter { teams ->
            state.selectedRacingCategories.any { selectedCategory ->
                teams.category.name == selectedCategory.getApiName()
            }
        }

        val filteredTeamsBySearchBar = if (state.isSearchBarVisible) {
            filteredTeamsByCategory.filter { team ->
                team.driver.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    team.codriver.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    team.name.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    team.number.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true)
            }
        } else {
            filteredTeamsByCategory
        }

        Column {
            filteredTeamsByCategory.forEach {
                if (filteredTeamsBySearchBar.contains(it)) {
                    TeamCard(
                        team = it,
                        navController = navController,
                    )
                }
            }
        }
    }
}