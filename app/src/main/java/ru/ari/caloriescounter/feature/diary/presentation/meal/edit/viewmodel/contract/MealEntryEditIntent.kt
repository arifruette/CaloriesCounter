package ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface MealEntryEditIntent : UiIntent {
    data class GramsChanged(val gramsInput: String) : MealEntryEditIntent
    data object SaveClicked : MealEntryEditIntent
}
