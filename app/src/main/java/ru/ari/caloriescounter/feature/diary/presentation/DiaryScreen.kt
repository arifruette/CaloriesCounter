package ru.ari.caloriescounter.feature.diary.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.domain.model.MealType

@Composable
fun DiaryRoute(
    contentPadding: PaddingValues,
    onNavigateToMealProducts: (MealType) -> Unit,
    onNavigateToWeightGoal: () -> Unit,
    viewModel: DiaryViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onIntent(DiaryIntent.ScreenOpened)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is DiaryEffect.NavigateToMealProducts -> onNavigateToMealProducts(effect.mealType)
                DiaryEffect.NavigateToWeightGoal -> onNavigateToWeightGoal()
            }
        }
    }

    DiaryScreen(
        state = state.value,
        contentPadding = contentPadding,
        onMealClick = { mealType -> viewModel.onIntent(DiaryIntent.MealClicked(mealType)) },
        onDecreaseCurrentWeight = { viewModel.onIntent(DiaryIntent.DecreaseCurrentWeight) },
        onIncreaseCurrentWeight = { viewModel.onIntent(DiaryIntent.IncreaseCurrentWeight) },
        onDecreaseCurrentWeightFast = { viewModel.onIntent(DiaryIntent.DecreaseCurrentWeightFast) },
        onIncreaseCurrentWeightFast = { viewModel.onIntent(DiaryIntent.IncreaseCurrentWeightFast) },
        onWeightCardClick = { viewModel.onIntent(DiaryIntent.WeightCardClicked) },
    )
}

@Composable
fun DiaryScreen(
    state: DiaryState,
    contentPadding: PaddingValues,
    onMealClick: (MealType) -> Unit,
    onDecreaseCurrentWeight: () -> Unit,
    onIncreaseCurrentWeight: () -> Unit,
    onDecreaseCurrentWeightFast: () -> Unit,
    onIncreaseCurrentWeightFast: () -> Unit,
    onWeightCardClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = contentPadding.calculateTopPadding() + 16.dp,
            end = 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 20.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.screen_diary),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = stringResource(R.string.diary_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        item {
            WeightCard(
                card = state.weightCard,
                onDecreaseCurrentWeight = onDecreaseCurrentWeight,
                onIncreaseCurrentWeight = onIncreaseCurrentWeight,
                onDecreaseCurrentWeightFast = onDecreaseCurrentWeightFast,
                onIncreaseCurrentWeightFast = onIncreaseCurrentWeightFast,
                onClick = onWeightCardClick,
            )
        }
        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        items(state.mealCards, key = { it.mealType.name }) { card ->
            MealCard(card = card, onClick = { onMealClick(card.mealType) })
        }
    }
}

@Composable
private fun WeightCard(
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

@Composable
private fun MealCard(
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
