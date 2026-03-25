package ru.ari.caloriescounter.feature.diary.presentation.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.diary.components.DiaryHeader
import ru.ari.caloriescounter.feature.diary.presentation.diary.components.DiaryWeightCard
import ru.ari.caloriescounter.feature.diary.presentation.diary.components.MealCard
import ru.ari.caloriescounter.feature.diary.presentation.diary.components.NutritionProgressCard
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryState

@Composable
fun DiaryScreen(
    state: DiaryState,
    contentPadding: PaddingValues,
    onMealClick: (mealKey: String, mealTitle: String) -> Unit,
    onNutritionGoalsClick: () -> Unit,
    onNewMealTitleChanged: (String) -> Unit,
    onAddMealClick: () -> Unit,
    onRenameMealClick: (mealKey: String, mealTitle: String) -> Unit,
    onEditingMealTitleChanged: (String) -> Unit,
    onConfirmRenameMealClick: () -> Unit,
    onDismissRenameMealClick: () -> Unit,
    onDeleteMealClick: (mealKey: String) -> Unit,
    onDecreaseCurrentWeight: () -> Unit,
    onIncreaseCurrentWeight: () -> Unit,
    onDecreaseCurrentWeightFast: () -> Unit,
    onIncreaseCurrentWeightFast: () -> Unit,
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
            DiaryHeader()
        }
        item {
            NutritionProgressCard(
                card = state.nutritionProgressCard,
                onEditGoalsClick = onNutritionGoalsClick,
            )
        }
        item {
            DiaryWeightCard(
                currentWeightKg = state.currentWeightKg,
                targetWeightKg = state.targetWeightKg,
                onDecreaseCurrentWeight = onDecreaseCurrentWeight,
                onIncreaseCurrentWeight = onIncreaseCurrentWeight,
                onDecreaseCurrentWeightFast = onDecreaseCurrentWeightFast,
                onIncreaseCurrentWeightFast = onIncreaseCurrentWeightFast,
            )
        }
        item {
            OutlinedTextField(
                value = state.newMealTitleInput,
                onValueChange = onNewMealTitleChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.diary_add_meal_label)) },
                singleLine = true,
                trailingIcon = {
                    TextButton(
                        onClick = onAddMealClick,
                        enabled = state.newMealTitleInput.trim().isNotEmpty(),
                    ) {
                        Text(text = stringResource(R.string.diary_add_meal_action))
                    }
                },
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
        items(state.mealCards, key = { it.mealKey }) { card ->
            MealCard(
                card = card,
                onClick = { onMealClick(card.mealKey, card.title) },
                onRenameClick = { onRenameMealClick(card.mealKey, card.title) },
                onDeleteClick = { onDeleteMealClick(card.mealKey) },
            )
        }
    }

    if (state.editingMealKey != null) {
        AlertDialog(
            onDismissRequest = onDismissRenameMealClick,
            title = { Text(text = stringResource(R.string.diary_meal_rename_action)) },
            text = {
                OutlinedTextField(
                    value = state.editingMealTitleInput,
                    onValueChange = onEditingMealTitleChanged,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(text = stringResource(R.string.diary_add_meal_label)) },
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirmRenameMealClick) {
                    Text(text = stringResource(R.string.action_save))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRenameMealClick) {
                    Text(text = stringResource(R.string.action_cancel))
                }
            },
        )
    }
}

