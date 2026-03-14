package ru.ari.caloriescounter.feature.stats.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.mvi.UiIntent
import ru.ari.caloriescounter.core.common.mvi.UiState
import ru.ari.caloriescounter.feature.stats.domain.StatsInteractor

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val interactor: StatsInteractor,
) : BaseMviViewModel<StatsIntent, StatsState, Nothing>(StatsState()) {

    override fun onIntent(intent: StatsIntent) {
        when (intent) {
            StatsIntent.ScreenOpened -> viewModelScope.launch {
                updateState { copy(isReady = true) }
            }
        }
    }
}

data class StatsState(
    val isReady: Boolean = false,
) : UiState

sealed interface StatsIntent : UiIntent {
    data object ScreenOpened : StatsIntent
}
