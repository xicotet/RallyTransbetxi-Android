package com.canolabs.rallytransbetxi.ui.teams

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.ui.miscellaneous.removeDiacriticalMarks

@Composable
fun TeamsContent(
    teams: List<Team>,
    isLoading: Boolean,
    state: TeamsScreenUIState
) {
    if (isLoading) {
        TeamCardShimmer()
    } else {
        val filteredTeamsByCategory = teams.filter { teams ->
            state.selectedRacingCategories.any { selectedCategory ->
                teams.category.name == stringResource(id = selectedCategory.getName())
            }
        }

        val sortedTeamsByNumber = filteredTeamsByCategory.sortedBy { it.number }

        val filteredTeamsBySearchBar = if (state.isSearchBarVisible) {
            sortedTeamsByNumber.filter { team ->
                team.driver.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    team.codriver.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    team.name.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true) ||
                    team.number.removeDiacriticalMarks().contains(state.searchText.removeDiacriticalMarks(), ignoreCase = true)
            }
        } else {
            sortedTeamsByNumber
        }

        Column {
            teams.forEach {
                if (filteredTeamsBySearchBar.contains(it)) {
                    TeamCard(team = it)
                }
            }
        }
    }
}