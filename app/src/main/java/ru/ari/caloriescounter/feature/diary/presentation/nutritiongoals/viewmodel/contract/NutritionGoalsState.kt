package ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

data class NutritionGoalsState(
    val caloriesInput: String = "",
    val proteinInput: String = "",
    val fatInput: String = "",
    val carbsInput: String = "",
    val showValidationErrors: Boolean = false,
    val isLoading: Boolean = true,
) : UiState {
    val caloriesHasError: Boolean
        get() = showValidationErrors && caloriesInput.toIntOrNull()?.let { it > 0 } != true

    val proteinHasError: Boolean
        get() = showValidationErrors && proteinInput.toDoubleOrNull()?.let { it > 0.0 } != true

    val fatHasError: Boolean
        get() = showValidationErrors && fatInput.toDoubleOrNull()?.let { it > 0.0 } != true

    val carbsHasError: Boolean
        get() = showValidationErrors && carbsInput.toDoubleOrNull()?.let { it > 0.0 } != true
}
