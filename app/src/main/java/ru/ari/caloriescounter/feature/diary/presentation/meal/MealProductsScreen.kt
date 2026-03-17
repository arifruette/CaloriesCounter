package ru.ari.caloriescounter.feature.diary.presentation.meal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.common.formatRuDecimal
import ru.ari.caloriescounter.feature.diary.presentation.common.titleRes
import ru.ari.caloriescounter.feature.diary.presentation.meal.model.MealEntryUiModel
import ru.ari.caloriescounter.feature.diary.presentation.meal.viewmodel.contract.MealProductsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealProductsScreen(
    state: MealProductsState,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onDeleteEntryClick: (Long) -> Unit,
    onAddProductClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(state.mealType.titleRes())) },
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
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.meal_products_add_fab),
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = contentPadding.calculateTopPadding() + innerPadding.calculateTopPadding() + 8.dp,
                    end = 16.dp,
                    bottom = contentPadding.calculateBottomPadding() + innerPadding.calculateBottomPadding() + 80.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                MealSummaryCard(
                    totalCalories = state.totalCalories,
                    totalProtein = state.totalProtein,
                    totalFat = state.totalFat,
                    totalCarbs = state.totalCarbs,
                )
            }
            if (state.entries.isEmpty() && !state.isLoading) {
                item {
                    Text(
                        text = stringResource(R.string.meal_products_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            items(state.entries, key = { it.id }) { entry ->
                MealEntryCard(
                    entry = entry,
                    onDeleteClick = { onDeleteEntryClick(entry.id) },
                )
            }
        }
    }
}

@Composable
private fun MealSummaryCard(
    totalCalories: Int,
    totalProtein: Double,
    totalFat: Double,
    totalCarbs: Double,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(R.string.meal_products_kbju_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MealMetricCell(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.meal_products_metric_calories),
                    value = stringResource(
                        R.string.meal_products_metric_kcal_value,
                        totalCalories,
                    ),
                )
                MealMetricCell(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.meal_products_metric_protein),
                    value = stringResource(
                        R.string.meal_products_metric_grams_value,
                        totalProtein.formatRuDecimal(),
                    ),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MealMetricCell(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.meal_products_metric_fat),
                    value = stringResource(
                        R.string.meal_products_metric_grams_value,
                        totalFat.formatRuDecimal(),
                    ),
                )
                MealMetricCell(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.meal_products_metric_carbs),
                    value = stringResource(
                        R.string.meal_products_metric_grams_value,
                        totalCarbs.formatRuDecimal(),
                    ),
                )
            }
        }
    }
}

@Composable
private fun MealMetricCell(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun MealEntryCard(
    entry: MealEntryUiModel,
    onDeleteClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = entry.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(
                        R.string.meal_products_entry_summary,
                        entry.grams.formatRuDecimal(),
                        entry.calories,
                        entry.protein.formatRuDecimal(),
                        entry.fat.formatRuDecimal(),
                        entry.carbs.formatRuDecimal(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.meal_products_delete_entry),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
