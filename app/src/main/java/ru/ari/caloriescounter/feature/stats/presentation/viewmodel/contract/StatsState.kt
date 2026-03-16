package ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

data class StatsState(
    val isReady: Boolean = false,
) : UiState
