package ru.ari.caloriescounter.core.navigation

sealed interface NavCommand {
    data class NavigateTo(
        val route: AppRoute,
        val singleTop: Boolean = true,
        val restoreState: Boolean = true,
    ) : NavCommand
}
