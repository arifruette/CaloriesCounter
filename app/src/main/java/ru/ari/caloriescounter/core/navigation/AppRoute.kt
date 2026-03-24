package ru.ari.caloriescounter.core.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    data object LaunchRoute : AppRoute

    @Serializable
    data object DiaryRoute : AppRoute

    @Serializable
    data object StatsRoute : AppRoute

    @Serializable
    data class MealProductsRoute(val mealType: String) : AppRoute

    @Serializable
    data class MealProductSearchRoute(val mealType: String) : AppRoute

    @Serializable
    data class ManualProductCreateRoute(val mealType: String) : AppRoute

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
    data class MealEntryEditRoute(
        val entryId: Long,
        val mealType: String,
        val entryName: String,
        val grams: Double,
    ) : AppRoute

    @Serializable
    data object WeightGoalRoute : AppRoute

    @Serializable
    data object NutritionGoalsRoute : AppRoute

    @Serializable
    data object OnboardingRoute : AppRoute

    @Serializable
    data object UserProfileRoute : AppRoute
}
