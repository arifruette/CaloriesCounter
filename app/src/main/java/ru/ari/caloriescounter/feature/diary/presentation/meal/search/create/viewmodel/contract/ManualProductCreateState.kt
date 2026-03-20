package ru.ari.caloriescounter.feature.diary.presentation.meal.search.create.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

data class ManualProductCreateState(
    val nameInput: String = "",
    val caloriesInput: String = "",
    val proteinInput: String = "",
    val fatInput: String = "",
    val carbsInput: String = "",
    val gramsInput: String = "",
    val nameHasError: Boolean = false,
    val caloriesHasError: Boolean = false,
    val proteinHasError: Boolean = false,
    val fatHasError: Boolean = false,
    val carbsHasError: Boolean = false,
    val gramsHasError: Boolean = false,
    val isSaving: Boolean = false,
) : UiState
