package ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState

data class LaunchGateState(
    val isLoading: Boolean = true,
) : UiState

