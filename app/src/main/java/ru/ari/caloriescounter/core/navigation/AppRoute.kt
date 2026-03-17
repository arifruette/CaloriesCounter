package ru.ari.caloriescounter.core.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    data object DiaryRoute : AppRoute

    @Serializable
    data object StatsRoute : AppRoute

    @Serializable
    data object RecipesRoute : AppRoute

    @Serializable
    data class MealProductsRoute(val mealType: String) : AppRoute

    @Serializable
    data class MealProductSearchRoute(val mealType: String) : AppRoute

    @Serializable
    data class MealProductDetailsRoute(
        val mealType: String,
        val source: String,
        val externalId: String?,
        val barcode: String?,
        val nameRu: String,
        val caloriesPer100g: Double,
        val proteinPer100g: Double,
        val fatPer100g: Double,
        val carbsPer100g: Double,
    ) : AppRoute

    @Serializable
    data object WeightGoalRoute : AppRoute

    @Serializable
    data object NutritionGoalsRoute : AppRoute
}
