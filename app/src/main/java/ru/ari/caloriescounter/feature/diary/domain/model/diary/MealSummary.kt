package ru.ari.caloriescounter.feature.diary.domain.model.diary

import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

data class MealSummary(
    val mealType: MealType,
    val entriesCount: Int,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
)
