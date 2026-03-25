package ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

data class NutritionGoalsState(
    val caloriesInput: String = "",
    val proteinInput: String = "",
    val fatInput: String = "",
    val carbsInput: String = "",
    val recommendedCalories: Int? = null,
    val recommendedProtein: Double? = null,
    val recommendedFat: Double? = null,
    val recommendedCarbs: Double? = null,
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

    val hasRecommendation: Boolean
        get() = recommendedCalories != null &&
            recommendedProtein != null &&
            recommendedFat != null &&
            recommendedCarbs != null

    val differsFromRecommendation: Boolean
        get() {
            val calories = recommendedCalories ?: return false
            val protein = recommendedProtein ?: return false
            val fat = recommendedFat ?: return false
            val carbs = recommendedCarbs ?: return false

            val currentCalories = caloriesInput.toIntOrNull() ?: return true
            val currentProtein = proteinInput.toDoubleOrNull() ?: return true
            val currentFat = fatInput.toDoubleOrNull() ?: return true
            val currentCarbs = carbsInput.toDoubleOrNull() ?: return true

            return currentCalories != calories ||
                kotlin.math.abs(currentProtein - protein) > 0.09 ||
                kotlin.math.abs(currentFat - fat) > 0.09 ||
                kotlin.math.abs(currentCarbs - carbs) > 0.09
        }
}
