package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface DiaryIntent : UiIntent {
    data object ScreenOpened : DiaryIntent
    data class MealClicked(val mealKey: String, val mealTitle: String) : DiaryIntent
    data object NutritionGoalsClicked : DiaryIntent
    data class NewMealTitleChanged(val value: String) : DiaryIntent
    data object AddMealClicked : DiaryIntent
    data class RenameMealClicked(val mealKey: String, val currentTitle: String) : DiaryIntent
    data class EditingMealTitleChanged(val value: String) : DiaryIntent
    data object ConfirmRenameMealClicked : DiaryIntent
    data object DismissRenameMealClicked : DiaryIntent
    data class DeleteMealClicked(val mealKey: String) : DiaryIntent
    data object UndoDeleteMealClicked : DiaryIntent
    data object DecreaseCurrentWeight : DiaryIntent
    data object IncreaseCurrentWeight : DiaryIntent
    data object DecreaseCurrentWeightFast : DiaryIntent
    data object IncreaseCurrentWeightFast : DiaryIntent
}
