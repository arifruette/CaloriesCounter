package ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.NutritionGoalsViewModel
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsEffect
import ru.ari.caloriescounter.feature.diary.presentation.nutritiongoals.viewmodel.contract.NutritionGoalsIntent

@Composable
fun NutritionGoalsRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    viewModel: NutritionGoalsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onIntent(NutritionGoalsIntent.ScreenOpened)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                NutritionGoalsEffect.Saved -> onBackClick()
            }
        }
    }

    NutritionGoalsScreen(
        state = state.value,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onCaloriesChanged = { viewModel.onIntent(NutritionGoalsIntent.CaloriesChanged(it)) },
        onProteinChanged = { viewModel.onIntent(NutritionGoalsIntent.ProteinChanged(it)) },
        onFatChanged = { viewModel.onIntent(NutritionGoalsIntent.FatChanged(it)) },
        onCarbsChanged = { viewModel.onIntent(NutritionGoalsIntent.CarbsChanged(it)) },
        onSaveClick = { viewModel.onIntent(NutritionGoalsIntent.SaveClicked) },
    )
}
