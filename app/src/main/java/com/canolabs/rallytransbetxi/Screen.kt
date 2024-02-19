package com.canolabs.rallytransbetxi

sealed class Screen(
    val route: String,
    val title: String,
    val iconSelected: Int,
    val iconUnselected: Int
) {
    object Home : Screen("home", "Etapas", R.drawable.map_filled, R.drawable.map_outlined)
    object Search : Screen("search", "Resultados", R.drawable.sports_score, R.drawable.sports_score)
    object Favorites : Screen("favorites", "Equipos", R.drawable.group_filled, R.drawable.group_outlined)
    object Profile : Screen("profile", "Rally", R.drawable.campaign_filled, R.drawable.campaign_outlined)
}