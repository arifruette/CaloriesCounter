package ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiEffect

sealed interface LaunchGateEffect : UiEffect {
    data object NavigateToOnboarding : LaunchGateEffect
    data object NavigateToDiary : LaunchGateEffect
}

