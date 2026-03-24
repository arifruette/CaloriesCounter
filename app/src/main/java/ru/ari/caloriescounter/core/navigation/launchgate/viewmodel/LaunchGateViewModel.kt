package ru.ari.caloriescounter.core.navigation.launchgate.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.contract.LaunchGateEffect
import ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.contract.LaunchGateIntent
import ru.ari.caloriescounter.core.navigation.launchgate.viewmodel.contract.LaunchGateState
import ru.ari.caloriescounter.feature.diary.domain.interactor.UserProfileInteractor

@HiltViewModel
class LaunchGateViewModel @Inject constructor(
    private val userProfileInteractor: UserProfileInteractor,
) : BaseMviViewModel<LaunchGateIntent, LaunchGateState, LaunchGateEffect>(LaunchGateState()) {

    private var handled = false

    override fun onIntent(intent: LaunchGateIntent) {
        when (intent) {
            LaunchGateIntent.ScreenOpened -> routeOnLaunch()
        }
    }

    private fun routeOnLaunch() {
        if (handled) return
        handled = true

        viewModelScope.launch {
            val onboardingCompleted = userProfileInteractor.observeOnboardingCompleted().first()
            updateState { copy(isLoading = false) }
            if (onboardingCompleted) {
                emitEffect(LaunchGateEffect.NavigateToDiary)
            } else {
                emitEffect(LaunchGateEffect.NavigateToOnboarding)
            }
        }
    }
}

