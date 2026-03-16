package ru.ari.caloriescounter.feature.diary.presentation.diary.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal
import ru.ari.caloriescounter.feature.diary.presentation.common.titleRes
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.MealCardUiModel

@Composable
fun MealCard(
    card: MealCardUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(card.mealType.titleRes()),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(
                    R.string.meal_summary,
                    card.calories,
                    card.protein.formatRuDecimal(),
                    card.fat.formatRuDecimal(),
                    card.carbs.formatRuDecimal(),
                    card.entriesCount,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
