package ru.ari.caloriescounter.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.core.navigation.launchgate.LaunchGateRoute
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.diary.DiaryRoute
import ru.ari.caloriescounter.feature.diary.presentation.meal.MealProductsRoute
import ru.ari.caloriescounter.feature.diary.presentation.meal.edit.MealEntryEditRoute
import ru.ari.caloriescounter.feature.diary.presentation.meal.details.ProductDetailsRoute
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.ProductSearchRoute
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.ManualProductCreateRoute
import ru.ari.caloriescounter.feature.diary.presentation.meal.search.model.ProductSearchItemUiModel
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.NutritionGoalsRoute
import ru.ari.caloriescounter.feature.diary.presentation.profile.ProfileRoute
import ru.ari.caloriescounter.feature.diary.presentation.userprofile.UserProfileRoute
import ru.ari.caloriescounter.feature.stats.presentation.StatsRoute

private data class TopLevelDestination(
    val route: AppRoute,
    val labelRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val topLevelDestinations = listOf(
    TopLevelDestination(AppRoute.DiaryRoute, R.string.tab_diary, Icons.Filled.Restaurant),
    TopLevelDestination(AppRoute.StatsRoute, R.string.tab_stats, Icons.Filled.AutoGraph),
    TopLevelDestination(AppRoute.ProfileRoute, R.string.tab_profile, Icons.Filled.Person),
)

@Composable
fun CaloriesCounterRoot(
    onStartupResolved: () -> Unit = {},
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isTopLevelDestination = currentDestination?.hierarchy?.any { destination ->
        topLevelDestinations.any { destination.hasRoute(it.route::class) }
    } == true
    val showBottomBar = currentDestination?.hierarchy?.none {
            it.hasRoute(AppRoute.MealProductsRoute::class) ||
            it.hasRoute(AppRoute.MealProductSearchRoute::class) ||
            it.hasRoute(AppRoute.ManualProductCreateRoute::class) ||
            it.hasRoute(AppRoute.MealProductDetailsRoute::class) ||
            it.hasRoute(AppRoute.MealEntryEditRoute::class) ||
            it.hasRoute(AppRoute.NutritionGoalsRoute::class) ||
            it.hasRoute(AppRoute.OnboardingRoute::class) ||
            it.hasRoute(AppRoute.UserProfileRoute::class) ||
            it.hasRoute(AppRoute.LaunchRoute::class)
    } != false
    val bottomInsetPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
        .asPaddingValues()
        .calculateBottomPadding()
    val bottomBarExtraPadding = 80.dp + bottomInsetPadding

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            CaloriesCounterNavHost(
                navController = navController,
                contentPadding = innerPadding.withBottomPadding(
                    extraBottom = if (isTopLevelDestination) bottomBarExtraPadding else 0.dp,
                ),
                onStartupResolved = onStartupResolved,
            )

            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
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
        }
    }
}

@Composable
private fun CaloriesCounterNavHost(
    navController: androidx.navigation.NavHostController,
    contentPadding: PaddingValues,
    onStartupResolved: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.LaunchRoute,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable<AppRoute.LaunchRoute> {
            LaunchGateRoute(
                onNavigateToOnboarding = {
                    navController.navigate(AppRoute.OnboardingRoute) {
                        popUpTo(AppRoute.LaunchRoute) { inclusive = true }
                    }
                },
                onNavigateToDiary = {
                    navController.navigate(AppRoute.DiaryRoute) {
                        popUpTo(AppRoute.LaunchRoute) { inclusive = true }
                    }
                },
                onStartupResolved = onStartupResolved,
            )
        }
        composable<AppRoute.OnboardingRoute> {
            UserProfileRoute(
                contentPadding = contentPadding,
                isOnboarding = true,
                onBackClick = null,
                onSaved = {
                    navController.navigate(AppRoute.DiaryRoute) {
                        popUpTo(AppRoute.OnboardingRoute) { inclusive = true }
                    }
                },
            )
        }
        composable<AppRoute.DiaryRoute> {
            DiaryRoute(
                contentPadding = contentPadding,
                onNavigateToMealProducts = { mealType ->
                    navController.navigate(AppRoute.MealProductsRoute(mealType.name))
                },
                onNavigateToNutritionGoals = {
                    navController.navigate(AppRoute.NutritionGoalsRoute)
                },
            )
        }
        composable<AppRoute.StatsRoute> {
            StatsRoute(contentPadding = contentPadding)
        }
        composable<AppRoute.ProfileRoute> {
            ProfileRoute(
                contentPadding = contentPadding,
                onNavigateToUserProfile = {
                    navController.navigate(AppRoute.UserProfileRoute)
                },
            )
        }
        composable<AppRoute.MealProductsRoute> {
            MealProductsRoute(
                contentPadding = contentPadding,
                onBackClick = { navController.popBackStack() },
                onNavigateToSearch = { mealType ->
                    navController.navigate(AppRoute.MealProductSearchRoute(mealType = mealType.name))
                },
                onNavigateToEntryEdit = { entryId, mealType, entryName, grams ->
                    navController.navigate(
                        AppRoute.MealEntryEditRoute(
                            entryId = entryId,
                            mealType = mealType.name,
                            entryName = entryName,
                            grams = grams,
                        ),
                    )
                },
            )
        }
        composable<AppRoute.MealProductSearchRoute> {
            ProductSearchRoute(
                contentPadding = contentPadding,
                onBackClick = { navController.popBackStack() },
                onNavigateToProductDetails = { mealType, product ->
                    navController.navigate(product.toProductDetailsRoute(mealType))
                },
                onNavigateToManualProductCreate = { mealType ->
                    navController.navigate(AppRoute.ManualProductCreateRoute(mealType = mealType.name))
                },
            )
        }
        composable<AppRoute.ManualProductCreateRoute> {
            ManualProductCreateRoute(
                contentPadding = contentPadding,
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }
        composable<AppRoute.MealProductDetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AppRoute.MealProductDetailsRoute>()
            ProductDetailsRoute(
                contentPadding = contentPadding,
                onBackClick = { navController.popBackStack() },
                onProductAdded = {
                    navController.popBackStack(
                        route = AppRoute.MealProductsRoute(route.mealType),
                        inclusive = false,
                    )
                },
            )
        }
        composable<AppRoute.MealEntryEditRoute> {
            MealEntryEditRoute(
                contentPadding = contentPadding,
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }
        composable<AppRoute.NutritionGoalsRoute> {
            NutritionGoalsRoute(
                contentPadding = contentPadding,
                onBackClick = { navController.popBackStack() },
            )
        }
        composable<AppRoute.UserProfileRoute> {
            UserProfileRoute(
                contentPadding = contentPadding,
                isOnboarding = false,
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }
    }
}

private fun ProductSearchItemUiModel.toProductDetailsRoute(mealType: MealType): AppRoute.MealProductDetailsRoute =
    AppRoute.MealProductDetailsRoute(
        mealType = mealType.name,
        source = source.name,
        externalId = externalId,
        barcode = barcode,
        nameRu = nameRu,
        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        carbsPer100g = carbsPer100g,
    )

private fun PaddingValues.withBottomPadding(extraBottom: androidx.compose.ui.unit.Dp): PaddingValues =
    PaddingValues(
        start = calculateStartPadding(LayoutDirection.Ltr),
        top = calculateTopPadding(),
        end = calculateEndPadding(LayoutDirection.Ltr),
        bottom = calculateBottomPadding() + extraBottom,
    )
