package ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

data class MealEntryEditState(
    val entryId: Long,
    val mealType: MealType,
    val entryName: String,
    val gramsInput: String,
    val hasInputError: Boolean = false,
) : UiState
