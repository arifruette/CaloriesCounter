package ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface LaunchGateIntent : UiIntent {
    data object ScreenOpened : LaunchGateIntent
}

