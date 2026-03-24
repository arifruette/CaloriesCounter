package ru.ari.caloriescounter.feature.diary.presentation.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.diary.components.DiaryHeader
import ru.ari.caloriescounter.feature.diary.presentation.diary.components.DiaryWeightCard
import ru.ari.caloriescounter.feature.diary.presentation.diary.components.MealCard
import ru.ari.caloriescounter.feature.diary.presentation.diary.components.NutritionProgressCard
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryState

@Composable
fun DiaryScreen(
    state: DiaryState,
    contentPadding: PaddingValues,
    onMealClick: (MealType) -> Unit,
    onNutritionGoalsClick: () -> Unit,
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
