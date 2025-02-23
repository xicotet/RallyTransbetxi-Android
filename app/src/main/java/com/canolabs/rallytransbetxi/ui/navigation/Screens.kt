package com.canolabs.rallytransbetxi.ui.navigation

import com.canolabs.rallytransbetxi.R

sealed class Screens(
    val route: String,
    val title: Int? = null,
    val iconSelected: Int? = null,
    val iconUnselected: Int? = null,
    val hasNavigationBar: Boolean = true
) {
    data object Onboarding : Screens("onboarding", hasNavigationBar = false)
    data object Rally : Screens("rally", R.string.rally, R.drawable.home_filled, R.drawable.home_outlined)
    data object Stages : Screens("stages", R.string.stages, R.drawable.map_filled, R.drawable.map_outlined)
    data object Results : Screens("results", R.string.results, R.drawable.sports_score, R.drawable.sports_score)
    data object Teams : Screens("teams", R.string.teams, R.drawable.group_filled, R.drawable.group_outlined)
    data object Maps : Screens("map", hasNavigationBar = false)
    data object HallOfFame : Screens("hallOfFame", hasNavigationBar = false)
    data object Sponsors : Screens("sponsors", hasNavigationBar = false)
    data object Eat : Screens("eat", hasNavigationBar = false)
    data object TeamDetail : Screens("teamDetail", hasNavigationBar = false)
    data object NewsDetail : Screens("newsDetail", hasNavigationBar = false)
}

fun shouldDisplayNavigationBar(route: String?): Boolean {
    return Screens::class.sealedSubclasses
        .mapNotNull { it.objectInstance }
        .firstOrNull {
            route?.contains(it.route) ?: true }
        ?.hasNavigationBar ?: true
}