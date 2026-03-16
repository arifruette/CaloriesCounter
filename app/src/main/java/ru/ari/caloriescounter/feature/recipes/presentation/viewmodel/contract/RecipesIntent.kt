package ru.ari.caloriescounter.feature.recipes.presentation.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface RecipesIntent : UiIntent {
    data object ScreenOpened : RecipesIntent
}
