package com.canolabs.rallytransbetxi.ui.teams

import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.canolabs.rallytransbetxi.ui.miscellaneous.UIState
import kotlinx.coroutines.flow.MutableStateFlow

data class TeamsScreenUIState(
    val teams: List<Team> = emptyList(),
    override val isLoading: Boolean = false,
    override val loadingMessageId: Int? = null,
) : UIState

fun MutableStateFlow<TeamsScreenUIState>.setIsLoading(isLoading: Boolean) {
    value = value.copy(isLoading = isLoading)
}

fun MutableStateFlow<TeamsScreenUIState>.setTeams(teams: List<Team>) {
    value = value.copy(teams = teams)
}