package ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect

sealed interface NutritionGoalsEffect : UiEffect {
    data object Saved : NutritionGoalsEffect
}
