package ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface NutritionGoalsIntent : UiIntent {
    data object ScreenOpened : NutritionGoalsIntent
    data class CaloriesChanged(val value: String) : NutritionGoalsIntent
    data class ProteinChanged(val value: String) : NutritionGoalsIntent
    data class FatChanged(val value: String) : NutritionGoalsIntent
    data class CarbsChanged(val value: String) : NutritionGoalsIntent
    data object SaveClicked : NutritionGoalsIntent
}
