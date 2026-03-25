package ru.ari.caloriescounter.feature.diary.domain.model.nutrition

data class NutritionRecommendation(
    val goals: NutritionGoals,
    val bmr: Double,
    val tdee: Double,
    val activityMultiplier: Double,
    val targetCaloriesBeforeClamping: Double,
)
