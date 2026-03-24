package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

sealed interface DiaryIntent : UiIntent {
    data object ScreenOpened : DiaryIntent
    data class MealClicked(val mealType: MealType) : DiaryIntent
    data object NutritionGoalsClicked : DiaryIntent
    data object DecreaseCurrentWeight : DiaryIntent
    data object IncreaseCurrentWeight : DiaryIntent
    data object DecreaseCurrentWeightFast : DiaryIntent
    data object IncreaseCurrentWeightFast : DiaryIntent
}
