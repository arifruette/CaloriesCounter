package ru.ari.caloriescounter.feature.diary.presentation.meal.details.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

data class ProductDetailsState(
    val mealType: MealType,
    val productName: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
    val gramsInput: String = "100",
    val calculatedCalories: Int = caloriesPer100g.toInt(),
    val calculatedProtein: Double = proteinPer100g,
    val calculatedFat: Double = fatPer100g,
    val calculatedCarbs: Double = carbsPer100g,
    val hasInputError: Boolean = false,
) : UiState
