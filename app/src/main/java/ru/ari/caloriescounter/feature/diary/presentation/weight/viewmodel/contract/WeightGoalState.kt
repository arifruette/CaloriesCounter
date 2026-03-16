package ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

data class WeightGoalState(
    val currentWeightKg: Double = 70.0,
    val targetWeightKg: Double = 65.0,
) : UiState
