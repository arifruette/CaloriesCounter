package ru.ari.caloriescounter.feature.stats.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.feature.stats.domain.interactor.StatsInteractor
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract.StatsEffect
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract.StatsIntent
import ru.ari.caloriescounter.feature.stats.presentation.viewmodel.contract.StatsState

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val interactor: StatsInteractor,
) : BaseMviViewModel<StatsIntent, StatsState, StatsEffect>(StatsState()) {

    override fun onIntent(intent: StatsIntent) {
        when (intent) {
            StatsIntent.ScreenOpened -> viewModelScope.launch {
                updateState { copy(isReady = true) }
            }
        }
    }
}
