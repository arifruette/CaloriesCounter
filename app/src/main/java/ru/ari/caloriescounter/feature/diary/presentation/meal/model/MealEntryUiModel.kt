package ru.ari.caloriescounter.feature.diary.presentation.meal.model

data class MealEntryUiModel(
    val id: Long,
    val name: String,
    val grams: Double,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
)
