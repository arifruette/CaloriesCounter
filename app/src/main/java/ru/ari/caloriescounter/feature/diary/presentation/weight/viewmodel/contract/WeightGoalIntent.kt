package ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiIntent

sealed interface WeightGoalIntent : UiIntent {
    data object ScreenOpened : WeightGoalIntent
    data object DecreaseCurrentWeight : WeightGoalIntent
    data object IncreaseCurrentWeight : WeightGoalIntent
    data object DecreaseCurrentWeightFast : WeightGoalIntent
    data object IncreaseCurrentWeightFast : WeightGoalIntent
    data object DecreaseTargetWeight : WeightGoalIntent
    data object IncreaseTargetWeight : WeightGoalIntent
    data object DecreaseTargetWeightFast : WeightGoalIntent
    data object IncreaseTargetWeightFast : WeightGoalIntent
}
