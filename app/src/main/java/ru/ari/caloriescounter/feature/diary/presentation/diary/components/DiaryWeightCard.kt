package ru.ari.caloriescounter.feature.diary.presentation.diary.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal
import ru.ari.caloriescounter.feature.diary.presentation.weight.components.WeightEditorCard

@Composable
fun DiaryWeightCard(
    currentWeightKg: Double,
    targetWeightKg: Double,
    onDecreaseCurrentWeight: () -> Unit,
    onIncreaseCurrentWeight: () -> Unit,
    onDecreaseCurrentWeightFast: () -> Unit,
    onIncreaseCurrentWeightFast: () -> Unit,
) {
    WeightEditorCard(
        label = stringResource(R.string.diary_weight_card_title),
        value = currentWeightKg,
        topCenteredText = stringResource(R.string.diary_weight_card_target, targetWeightKg.formatRuDecimal()),
        centerLabel = false,
        decrementContentDescription = stringResource(R.string.cd_decrease_current_weight),
        incrementContentDescription = stringResource(R.string.cd_increase_current_weight),
        onDecrease = onDecreaseCurrentWeight,
        onIncrease = onIncreaseCurrentWeight,
        onDecreaseFast = onDecreaseCurrentWeightFast,
        onIncreaseFast = onIncreaseCurrentWeightFast,
    )
}
