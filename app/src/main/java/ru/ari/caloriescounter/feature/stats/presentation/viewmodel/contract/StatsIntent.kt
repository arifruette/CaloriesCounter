package ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface StatsIntent : UiIntent {
    data object ScreenOpened : StatsIntent
}
