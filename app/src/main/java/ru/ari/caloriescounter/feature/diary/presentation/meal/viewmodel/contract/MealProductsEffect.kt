package ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

sealed interface MealProductsEffect : UiEffect {
    data class NavigateToSearch(val mealType: MealType) : MealProductsEffect
    data class NavigateToEntryEdit(
        val entryId: Long,
        val mealType: MealType,
        val entryName: String,
        val grams: Double,
    ) : MealProductsEffect
    data object ShowUndoDelete : MealProductsEffect
}
