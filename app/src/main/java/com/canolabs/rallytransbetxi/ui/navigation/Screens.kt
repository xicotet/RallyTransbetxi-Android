package com.canolabs.rallytransbetxi.ui.navigation

import com.canolabs.rallytransbetxi.R

sealed class Screens(
    val route: String,
    val title: Int,
    val iconSelected: Int,
    val iconUnselected: Int
) {

    object Rally : Screens("rally", R.string.rally, R.drawable.home_filled, R.drawable.home_outlined)
    object Stages : Screens("stages", R.string.stages, R.drawable.map_filled, R.drawable.map_outlined)
    object Results : Screens("results", R.string.results, R.drawable.sports_score, R.drawable.sports_score)
    object Teams : Screens("teams", R.string.teams, R.drawable.group_filled, R.drawable.group_outlined)
}