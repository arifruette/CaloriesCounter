package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model

import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

data class MealCardUiModel(
    val mealType: MealType,
    val calories: Int = 0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val entriesCount: Int = 0,
)
