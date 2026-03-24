package ru.ari.caloriescounter.feature.diary.presentation.diary

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.DiaryViewModel
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryEffect
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryIntent

@Composable
fun DiaryRoute(
    contentPadding: PaddingValues,
    onNavigateToMealProducts: (MealType) -> Unit,
    onNavigateToNutritionGoals: () -> Unit,
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
                DiaryEffect.NavigateToNutritionGoals -> onNavigateToNutritionGoals()
            }
        }
    }

    DiaryScreen(
        state = state.value,
        contentPadding = contentPadding,
        onMealClick = { mealType -> viewModel.onIntent(DiaryIntent.MealClicked(mealType)) },
        onNutritionGoalsClick = { viewModel.onIntent(DiaryIntent.NutritionGoalsClicked) },
        onDecreaseCurrentWeight = { viewModel.onIntent(DiaryIntent.DecreaseCurrentWeight) },
        onIncreaseCurrentWeight = { viewModel.onIntent(DiaryIntent.IncreaseCurrentWeight) },
        onDecreaseCurrentWeightFast = { viewModel.onIntent(DiaryIntent.DecreaseCurrentWeightFast) },
        onIncreaseCurrentWeightFast = { viewModel.onIntent(DiaryIntent.IncreaseCurrentWeightFast) },
    )
}
