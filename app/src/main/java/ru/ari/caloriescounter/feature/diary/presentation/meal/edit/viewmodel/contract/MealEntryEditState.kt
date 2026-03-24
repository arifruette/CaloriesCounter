package ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

data class MealEntryEditState(
    val entryId: Long,
    val mealKey: String,
    val mealTitle: String,
    val entryName: String,
    val gramsInput: String,
    val hasInputError: Boolean = false,
) : UiState
