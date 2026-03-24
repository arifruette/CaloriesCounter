package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

sealed interface DiaryEffect : UiEffect {
    data class NavigateToMealProducts(val mealType: MealType) : DiaryEffect
    data object NavigateToWeightGoal : DiaryEffect
    data object NavigateToNutritionGoals : DiaryEffect
    data object NavigateToUserProfile : DiaryEffect
}
