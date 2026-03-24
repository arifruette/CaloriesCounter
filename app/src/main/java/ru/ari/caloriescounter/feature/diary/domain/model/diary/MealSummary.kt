package ru.ari.caloriescounter.feature.diary.domain.model.diary

data class MealSummary(
    val mealKey: String,
    val title: String,
    val entriesCount: Int,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
)
