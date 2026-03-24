package ru.ari.caloriescounter.feature.diary.presentation.diary

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.flow.collectLatest
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.DiaryViewModel
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryEffect
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract.DiaryIntent

@Composable
fun DiaryRoute(
    contentPadding: PaddingValues,
    onNavigateToMealProducts: (mealKey: String, mealTitle: String) -> Unit,
    onNavigateToNutritionGoals: () -> Unit,
    viewModel: DiaryViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.onIntent(DiaryIntent.ScreenOpened)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is DiaryEffect.NavigateToMealProducts -> {
                    onNavigateToMealProducts(effect.mealKey, effect.mealTitle)
                }
                DiaryEffect.NavigateToNutritionGoals -> onNavigateToNutritionGoals()
                DiaryEffect.ShowUndoDeleteMeal -> {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.diary_meal_deleted_message),
                        actionLabel = context.getString(R.string.action_undo),
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onIntent(DiaryIntent.UndoDeleteMealClicked)
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        DiaryScreen(
            state = state.value,
            contentPadding = contentPadding,
            onMealClick = { mealKey, mealTitle -> viewModel.onIntent(DiaryIntent.MealClicked(mealKey, mealTitle)) },
            onNutritionGoalsClick = { viewModel.onIntent(DiaryIntent.NutritionGoalsClicked) },
            onNewMealTitleChanged = { viewModel.onIntent(DiaryIntent.NewMealTitleChanged(it)) },
            onAddMealClick = { viewModel.onIntent(DiaryIntent.AddMealClicked) },
            onRenameMealClick = { mealKey, mealTitle ->
                viewModel.onIntent(DiaryIntent.RenameMealClicked(mealKey, mealTitle))
            },
            onEditingMealTitleChanged = { viewModel.onIntent(DiaryIntent.EditingMealTitleChanged(it)) },
            onConfirmRenameMealClick = { viewModel.onIntent(DiaryIntent.ConfirmRenameMealClicked) },
            onDismissRenameMealClick = { viewModel.onIntent(DiaryIntent.DismissRenameMealClicked) },
            onDeleteMealClick = { mealKey -> viewModel.onIntent(DiaryIntent.DeleteMealClicked(mealKey)) },
            onDecreaseCurrentWeight = { viewModel.onIntent(DiaryIntent.DecreaseCurrentWeight) },
            onIncreaseCurrentWeight = { viewModel.onIntent(DiaryIntent.IncreaseCurrentWeight) },
            onDecreaseCurrentWeightFast = { viewModel.onIntent(DiaryIntent.DecreaseCurrentWeightFast) },
            onIncreaseCurrentWeightFast = { viewModel.onIntent(DiaryIntent.IncreaseCurrentWeightFast) },
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = contentPadding.calculateBottomPadding() + 24.dp),
        )
    }
}

