package ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect

sealed interface MealProductsEffect : UiEffect {
    data class NavigateToSearch(
        val mealKey: String,
        val mealTitle: String,
    ) : MealProductsEffect
    data class NavigateToEntryEdit(
        val entryId: Long,
        val mealKey: String,
        val mealTitle: String,
        val entryName: String,
        val grams: Double,
    ) : MealProductsEffect
    data object ShowUndoDelete : MealProductsEffect
}
