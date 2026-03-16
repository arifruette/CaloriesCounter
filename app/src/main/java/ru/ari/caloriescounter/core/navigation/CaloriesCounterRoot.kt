package ru.ari.caloriescounter.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.diary.DiaryRoute
import ru.ari.caloriescounter.feature.diary.presentation.meal.MealProductsPlaceholderScreen
import ru.ari.caloriescounter.feature.diary.presentation.weight.WeightGoalRoute
import ru.ari.caloriescounter.feature.recipes.presentation.RecipesRoute
import ru.ari.caloriescounter.feature.stats.presentation.StatsRoute

private data class TopLevelDestination(
    val route: AppRoute,
    val labelRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val topLevelDestinations = listOf(
    TopLevelDestination(AppRoute.DiaryRoute, R.string.tab_diary, Icons.Filled.Restaurant),
    TopLevelDestination(AppRoute.StatsRoute, R.string.tab_stats, Icons.Filled.AutoGraph),
    TopLevelDestination(AppRoute.RecipesRoute, R.string.tab_recipes, Icons.Filled.Book),
)

@Composable
fun CaloriesCounterRoot() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.hierarchy?.none {
        it.hasRoute(AppRoute.MealProductsRoute::class) ||
            it.hasRoute(AppRoute.WeightGoalRoute::class)
    } != false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    tonalElevation = 0.dp,
                ) {
                    topLevelDestinations.forEach { destination ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.hasRoute(destination.route::class)
                        } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = stringResource(destination.labelRes),
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(destination.labelRes),
                                    style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                indicatorColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        CaloriesCounterNavHost(
            navController = navController,
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun CaloriesCounterNavHost(
    navController: androidx.navigation.NavHostController,
    contentPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.DiaryRoute,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable<AppRoute.DiaryRoute> {
            DiaryRoute(
                contentPadding = contentPadding,
                onNavigateToMealProducts = { mealType ->
                    navController.navigate(AppRoute.MealProductsRoute(mealType.name))
                },
                onNavigateToWeightGoal = {
                    navController.navigate(AppRoute.WeightGoalRoute)
                },
            )
        }
        composable<AppRoute.StatsRoute> {
            StatsRoute(contentPadding = contentPadding)
        }
        composable<AppRoute.RecipesRoute> {
            RecipesRoute(contentPadding = contentPadding)
        }
        composable<AppRoute.MealProductsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AppRoute.MealProductsRoute>()
            MealProductsPlaceholderScreen(
                mealType = route.mealType.toMealTypeOrDefault(),
                contentPadding = contentPadding,
                onBackClick = { navController.popBackStack() },
            )
        }
        composable<AppRoute.WeightGoalRoute> {
            WeightGoalRoute(
                contentPadding = contentPadding,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}

private fun String.toMealTypeOrDefault(): MealType =
    runCatching { MealType.valueOf(this) }.getOrElse { MealType.BREAKFAST }
