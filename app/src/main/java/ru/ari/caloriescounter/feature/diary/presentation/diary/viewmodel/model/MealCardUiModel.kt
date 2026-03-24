package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model

data class MealCardUiModel(
    val mealKey: String,
    val title: String,
    val isBase: Boolean,
    val calories: Int = 0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val entriesCount: Int = 0,
)
