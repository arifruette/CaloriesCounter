package ru.ari.caloriescounter.feature.diary.presentation.weight

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel.WeightGoalViewModel
import ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel.contract.WeightGoalIntent

@Composable
fun WeightGoalRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    viewModel: WeightGoalViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onIntent(WeightGoalIntent.ScreenOpened)
    }

    WeightGoalScreen(
        state = state.value,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onDecreaseCurrentWeight = { viewModel.onIntent(WeightGoalIntent.DecreaseCurrentWeight) },
        onIncreaseCurrentWeight = { viewModel.onIntent(WeightGoalIntent.IncreaseCurrentWeight) },
        onDecreaseCurrentWeightFast = { viewModel.onIntent(WeightGoalIntent.DecreaseCurrentWeightFast) },
        onIncreaseCurrentWeightFast = { viewModel.onIntent(WeightGoalIntent.IncreaseCurrentWeightFast) },
        onDecreaseTargetWeight = { viewModel.onIntent(WeightGoalIntent.DecreaseTargetWeight) },
        onIncreaseTargetWeight = { viewModel.onIntent(WeightGoalIntent.IncreaseTargetWeight) },
        onDecreaseTargetWeightFast = { viewModel.onIntent(WeightGoalIntent.DecreaseTargetWeightFast) },
        onIncreaseTargetWeightFast = { viewModel.onIntent(WeightGoalIntent.IncreaseTargetWeightFast) },
    )
}
