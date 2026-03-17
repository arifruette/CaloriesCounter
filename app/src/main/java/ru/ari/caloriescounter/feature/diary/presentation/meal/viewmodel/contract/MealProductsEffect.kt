package ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

sealed interface MealProductsEffect : UiEffect {
    data class NavigateToSearch(val mealType: MealType) : MealProductsEffect
    data object ShowUndoDelete : MealProductsEffect
}
