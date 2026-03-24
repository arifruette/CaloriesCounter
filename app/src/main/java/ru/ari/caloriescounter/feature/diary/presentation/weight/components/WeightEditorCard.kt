package ru.ari.caloriescounter.feature.diary.presentation.weight.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.common.WeightAdjustmentButton
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal

@Composable
fun WeightEditorCard(
    label: String,
    value: Double,
    topCenteredText: String? = null,
    supportingText: String? = null,
    centerLabel: Boolean = true,
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
                textAlign = if (centerLabel) TextAlign.Center else TextAlign.Start,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (!topCenteredText.isNullOrBlank()) {
                Text(
                    text = topCenteredText,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WeightAdjustmentButton(
                    label = "-",
                    contentDescription = decrementContentDescription,
                    onTap = onDecrease,
                    onLongPressStep = onDecreaseFast,
                    size = 48.dp,
                )
                Text(
                    text = stringResource(R.string.weight_value, value.formatRuDecimal()),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                WeightAdjustmentButton(
                    label = "+",
                    contentDescription = incrementContentDescription,
                    onTap = onIncrease,
                    onLongPressStep = onIncreaseFast,
                    size = 48.dp,
                )
            }
            if (!supportingText.isNullOrBlank()) {
                Text(
                    text = supportingText,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
