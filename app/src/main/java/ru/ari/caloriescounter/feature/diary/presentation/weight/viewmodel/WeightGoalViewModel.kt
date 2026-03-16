package ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.feature.diary.domain.interactor.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel.contract.WeightGoalEffect
import ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel.contract.WeightGoalIntent
import ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel.contract.WeightGoalState

@HiltViewModel
class WeightGoalViewModel @Inject constructor(
    private val weightProfileInteractor: WeightProfileInteractor,
) : BaseMviViewModel<WeightGoalIntent, WeightGoalState, WeightGoalEffect>(WeightGoalState()) {

    private var observeWeightJob: Job? = null

    override fun onIntent(intent: WeightGoalIntent) {
        when (intent) {
            WeightGoalIntent.ScreenOpened -> observeWeightProfile()
            WeightGoalIntent.DecreaseCurrentWeight -> updateCurrentWeight(-WEIGHT_STEP_KG)
            WeightGoalIntent.IncreaseCurrentWeight -> updateCurrentWeight(WEIGHT_STEP_KG)
            WeightGoalIntent.DecreaseCurrentWeightFast -> updateCurrentWeight(-WEIGHT_LONG_PRESS_STEP_KG)
            WeightGoalIntent.IncreaseCurrentWeightFast -> updateCurrentWeight(WEIGHT_LONG_PRESS_STEP_KG)
            WeightGoalIntent.DecreaseTargetWeight -> updateTargetWeight(-WEIGHT_STEP_KG)
            WeightGoalIntent.IncreaseTargetWeight -> updateTargetWeight(WEIGHT_STEP_KG)
            WeightGoalIntent.DecreaseTargetWeightFast -> updateTargetWeight(-WEIGHT_LONG_PRESS_STEP_KG)
            WeightGoalIntent.IncreaseTargetWeightFast -> updateTargetWeight(WEIGHT_LONG_PRESS_STEP_KG)
        }
    }

    private fun observeWeightProfile() {
        if (observeWeightJob != null) return

        observeWeightJob = viewModelScope.launch {
            weightProfileInteractor.observeWeightProfile().collect { profile ->
                updateState {
                    copy(
                        currentWeightKg = profile.currentWeightKg,
                        targetWeightKg = profile.targetWeightKg,
                    )
                }
            }
        }
    }

    private fun updateCurrentWeight(delta: Double) {
        val updatedWeight = state.value.currentWeightKg + delta
        viewModelScope.launch {
            weightProfileInteractor.updateCurrentWeight(updatedWeight)
        }
    }

    private fun updateTargetWeight(delta: Double) {
        val updatedWeight = state.value.targetWeightKg + delta
        viewModelScope.launch {
            weightProfileInteractor.updateTargetWeight(updatedWeight)
        }
    }
}

private const val WEIGHT_STEP_KG = 0.1
private const val WEIGHT_LONG_PRESS_STEP_KG = 1.0
