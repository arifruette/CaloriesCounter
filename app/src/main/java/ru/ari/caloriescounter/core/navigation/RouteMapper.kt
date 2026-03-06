package ru.ari.caloriescounter.core.navigation

import ru.ari.caloriescounter.R

interface RouteMapper {
    fun toDestinationId(route: AppRoute): Int
    fun toMenuItemId(route: AppRoute): Int
    fun fromMenuItemId(itemId: Int): AppRoute
}

class DefaultRouteMapper : RouteMapper {
    override fun toDestinationId(route: AppRoute): Int = when (route) {
        AppRoute.DiaryRoute -> R.id.diaryFragment
        AppRoute.StatsRoute -> R.id.statsFragment
        AppRoute.RecipesRoute -> R.id.recipesFragment
    }

    override fun toMenuItemId(route: AppRoute): Int = when (route) {
        AppRoute.DiaryRoute -> R.id.menu_diary
        AppRoute.StatsRoute -> R.id.menu_stats
        AppRoute.RecipesRoute -> R.id.menu_recipes
    }

    override fun fromMenuItemId(itemId: Int): AppRoute = when (itemId) {
        R.id.menu_diary -> AppRoute.DiaryRoute
        R.id.menu_stats -> AppRoute.StatsRoute
        R.id.menu_recipes -> AppRoute.RecipesRoute
        else -> AppRoute.DiaryRoute
    }
}
