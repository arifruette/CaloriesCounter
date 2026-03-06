package ru.ari.caloriescounter.core.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    data object DiaryRoute : AppRoute

    @Serializable
    data object StatsRoute : AppRoute

    @Serializable
    data object RecipesRoute : AppRoute
}
