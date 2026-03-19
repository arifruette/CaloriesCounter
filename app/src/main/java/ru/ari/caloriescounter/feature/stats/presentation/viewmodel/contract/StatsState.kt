package ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.model.WeeklySummaryUiModel

data class StatsState(
    val isLoading: Boolean = true,
    val weeklySummary: WeeklySummaryUiModel? = null,
    val isEmpty: Boolean = false,
) : UiState
