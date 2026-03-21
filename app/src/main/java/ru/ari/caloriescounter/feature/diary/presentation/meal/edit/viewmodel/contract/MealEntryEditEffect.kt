package ru.ari.caloriescounter.feature.diary.presentation.meal.edit.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect

sealed interface MealEntryEditEffect : UiEffect {
    data object Saved : MealEntryEditEffect
}
