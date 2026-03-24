package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.DiaryNutritionProgressCardUiModel
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.MealCardUiModel

data class DiaryState(
    val nutritionProgressCard: DiaryNutritionProgressCardUiModel = DiaryNutritionProgressCardUiModel(),
    val currentWeightKg: Double = 70.0,
    val targetWeightKg: Double = 65.0,
    val mealCards: List<MealCardUiModel> = emptyList(),
    val newMealTitleInput: String = "",
    val editingMealKey: String? = null,
    val editingMealTitleInput: String = "",
    val isLoading: Boolean = true,
) : UiState
