package ru.ari.caloriescounter.core.navigation.launchgate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.LaunchGateViewModel
import ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.contract.LaunchGateEffect
import ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.contract.LaunchGateIntent

@Composable
fun LaunchGateRoute(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToDiary: () -> Unit,
    onStartupResolved: () -> Unit,
    viewModel: LaunchGateViewModel = hiltViewModel(),
) {
    LaunchedEffect(viewModel) {
        viewModel.onIntent(LaunchGateIntent.ScreenOpened)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            onStartupResolved()
            when (effect) {
                LaunchGateEffect.NavigateToOnboarding -> onNavigateToOnboarding()
                LaunchGateEffect.NavigateToDiary -> onNavigateToDiary()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize())
}
