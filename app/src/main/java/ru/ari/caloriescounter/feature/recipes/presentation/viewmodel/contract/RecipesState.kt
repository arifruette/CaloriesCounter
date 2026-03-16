package ru.ari.caloriescounter.feature.recipes.presentation.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

data class RecipesState(
    val isReady: Boolean = false,
) : UiState
