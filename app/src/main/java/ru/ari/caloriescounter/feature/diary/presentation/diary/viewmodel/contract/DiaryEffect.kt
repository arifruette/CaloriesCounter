package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect

sealed interface DiaryEffect : UiEffect {
    data class NavigateToMealProducts(
        val mealKey: String,
        val mealTitle: String,
    ) : DiaryEffect
    data object ShowUndoDeleteMeal : DiaryEffect
    data object NavigateToNutritionGoals : DiaryEffect
}
