package ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface MealProductsIntent : UiIntent {
    data object ScreenOpened : MealProductsIntent
    data class EntryClicked(val entryId: Long) : MealProductsIntent
    data class DeleteEntryClicked(val entryId: Long) : MealProductsIntent
    data object UndoDeleteClicked : MealProductsIntent
    data object AddProductClicked : MealProductsIntent
}
