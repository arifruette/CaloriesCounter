package ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.presentation.meal.model.MealEntryUiModel

data class MealProductsState(
    val mealKey: String,
    val mealTitle: String,
    val isLoading: Boolean = true,
    val entries: List<MealEntryUiModel> = emptyList(),
    val totalCalories: Int = 0,
    val totalProtein: Double = 0.0,
    val totalFat: Double = 0.0,
    val totalCarbs: Double = 0.0,
) : UiState
