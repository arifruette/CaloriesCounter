package ru.ari.caloriescounter.feature.diary.presentation.diary.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.DiaryMacroProgressUiModel
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.DiaryNutritionProgressCardUiModel

@Composable
fun NutritionProgressCard(
    card: DiaryNutritionProgressCardUiModel,
    onEditGoalsClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiaryContainer),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.diary_nutrition_progress_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            NutritionProgressRow(
                label = stringResource(R.string.meal_products_metric_calories),
                metric = card.calories,
                valueText = stringResource(
                    R.string.diary_nutrition_progress_kcal,
                    card.calories.current.toInt(),
                    card.calories.goal.toInt(),
                ),
            )
            NutritionProgressRow(
                label = stringResource(R.string.meal_products_metric_protein),
                metric = card.protein,
                valueText = stringResource(
                    R.string.diary_nutrition_progress_grams,
                    card.protein.current.formatRuDecimal(),
                    card.protein.goal.formatRuDecimal(),
                ),
            )
            NutritionProgressRow(
                label = stringResource(R.string.meal_products_metric_fat),
                metric = card.fat,
                valueText = stringResource(
                    R.string.diary_nutrition_progress_grams,
                    card.fat.current.formatRuDecimal(),
                    card.fat.goal.formatRuDecimal(),
                ),
            )
            NutritionProgressRow(
                label = stringResource(R.string.meal_products_metric_carbs),
                metric = card.carbs,
                valueText = stringResource(
                    R.string.diary_nutrition_progress_grams,
                    card.carbs.current.formatRuDecimal(),
                    card.carbs.goal.formatRuDecimal(),
                ),
            )
            Button(
                onClick = onEditGoalsClick,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(text = stringResource(R.string.diary_nutrition_progress_edit))
            }
        }
    }
}

@Composable
private fun NutritionProgressRow(
    label: String,
    metric: DiaryMacroProgressUiModel,
    valueText: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = valueText,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        LinearProgressIndicator(
            progress = { metric.progress },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        )
    }
}
