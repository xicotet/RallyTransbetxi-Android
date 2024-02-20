package com.canolabs.rallytransbetxi.ui.navigation

import com.canolabs.rallytransbetxi.R

sealed class Screens(
    val route: String,
    val title: String,
    val iconSelected: Int,
    val iconUnselected: Int
) {

    object Rally : Screens("rally", "Rally", R.drawable.home_filled, R.drawable.home_outlined)
    object Stages : Screens("stages", "Etapas", R.drawable.map_filled, R.drawable.map_outlined)
    object Results : Screens("results", "Resultados", R.drawable.sports_score, R.drawable.sports_score)
    object Teams : Screens("teams", "Equipos", R.drawable.group_filled, R.drawable.group_outlined)
}