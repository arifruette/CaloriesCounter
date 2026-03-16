package ru.ari.caloriescounter.feature.diary.presentation.diary.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.common.WeightAdjustmentButton
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.WeightCardUiModel

@Composable
fun WeightCard(
    card: WeightCardUiModel,
    onDecreaseCurrentWeight: () -> Unit,
    onIncreaseCurrentWeight: () -> Unit,
    onDecreaseCurrentWeightFast: () -> Unit,
    onIncreaseCurrentWeightFast: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.diary_weight_card_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.weight_current_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(
                            R.string.weight_value,
                            card.currentWeightKg.formatRuDecimal(),
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WeightAdjustmentButton(
                        label = "-",
                        contentDescription = stringResource(R.string.cd_decrease_current_weight),
                        onTap = onDecreaseCurrentWeight,
                        onLongPressStep = onDecreaseCurrentWeightFast,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    WeightAdjustmentButton(
                        label = "+",
                        contentDescription = stringResource(R.string.cd_increase_current_weight),
                        onTap = onIncreaseCurrentWeight,
                        onLongPressStep = onIncreaseCurrentWeightFast,
                    )
                }
            }
            Text(
                text = stringResource(
                    R.string.diary_weight_card_target,
                    card.targetWeightKg.formatRuDecimal(),
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
