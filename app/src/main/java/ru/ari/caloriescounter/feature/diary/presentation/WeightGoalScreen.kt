package ru.ari.caloriescounter.feature.diary.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.core.common.mvi.BaseMviViewModel
import ru.ari.caloriescounter.core.common.mvi.UiIntent
import ru.ari.caloriescounter.core.common.mvi.UiState
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileInteractor

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightGoalScreen(
    state: WeightGoalState,
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onDecreaseCurrentWeight: () -> Unit,
    onIncreaseCurrentWeight: () -> Unit,
    onDecreaseCurrentWeightFast: () -> Unit,
    onIncreaseCurrentWeightFast: () -> Unit,
    onDecreaseTargetWeight: () -> Unit,
    onIncreaseTargetWeight: () -> Unit,
    onDecreaseTargetWeightFast: () -> Unit,
    onIncreaseTargetWeightFast: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_weight_goal),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    top = contentPadding.calculateTopPadding() + innerPadding.calculateTopPadding() + 24.dp,
                    end = 24.dp,
                    bottom = contentPadding.calculateBottomPadding() + innerPadding.calculateBottomPadding() + 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            WeightEditorCard(
                label = stringResource(R.string.weight_current_label),
                value = state.currentWeightKg,
                decrementContentDescription = stringResource(R.string.cd_decrease_current_weight),
                incrementContentDescription = stringResource(R.string.cd_increase_current_weight),
                onDecrease = onDecreaseCurrentWeight,
                onIncrease = onIncreaseCurrentWeight,
                onDecreaseFast = onDecreaseCurrentWeightFast,
                onIncreaseFast = onIncreaseCurrentWeightFast,
            )
            WeightEditorCard(
                label = stringResource(R.string.weight_target_label),
                value = state.targetWeightKg,
                decrementContentDescription = stringResource(R.string.cd_decrease_target_weight),
                incrementContentDescription = stringResource(R.string.cd_increase_target_weight),
                onDecrease = onDecreaseTargetWeight,
                onIncrease = onIncreaseTargetWeight,
                onDecreaseFast = onDecreaseTargetWeightFast,
                onIncreaseFast = onIncreaseTargetWeightFast,
            )
        }
    }
}

@Composable
private fun WeightEditorCard(
    label: String,
    value: Double,
    decrementContentDescription: String,
    incrementContentDescription: String,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onDecreaseFast: () -> Unit,
    onIncreaseFast: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WeightActionButton(
                    label = "-",
                    contentDescription = decrementContentDescription,
                    onTap = onDecrease,
                    onLongPressStep = onDecreaseFast,
                )
                Text(
                    text = stringResource(R.string.weight_value, value.formatRuDecimal()),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                WeightActionButton(
                    label = "+",
                    contentDescription = incrementContentDescription,
                    onTap = onIncrease,
                    onLongPressStep = onIncreaseFast,
                )
            }
        }
    }
}

@Composable
private fun WeightActionButton(
    label: String,
    contentDescription: String,
    onTap: () -> Unit,
    onLongPressStep: () -> Unit,
) {
    WeightAdjustmentButton(
        label = label,
        contentDescription = contentDescription,
        onTap = onTap,
        onLongPressStep = onLongPressStep,
        size = 48.dp,
    )
}

@HiltViewModel
class WeightGoalViewModel @Inject constructor(
    private val weightProfileInteractor: WeightProfileInteractor,
) : BaseMviViewModel<WeightGoalIntent, WeightGoalState, Nothing>(WeightGoalState()) {

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

data class WeightGoalState(
    val currentWeightKg: Double = 70.0,
    val targetWeightKg: Double = 65.0,
) : UiState

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

private const val WEIGHT_STEP_KG = 0.1
private const val WEIGHT_LONG_PRESS_STEP_KG = 1.0
