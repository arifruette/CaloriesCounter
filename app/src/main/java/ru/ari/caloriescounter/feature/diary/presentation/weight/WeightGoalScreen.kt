package ru.ari.caloriescounter.feature.diary.presentation.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.weight.components.WeightEditorCard
import ru.ari.caloriescounter.feature.diary.presentation.weight.viewmodel.contract.WeightGoalState

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
